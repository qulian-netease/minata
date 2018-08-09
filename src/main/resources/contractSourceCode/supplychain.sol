
library SafeMath{

    function mul(uint8 a , uint8 b ) internal returns( uint8 ){
        if(a == 0 ){
            return 0;
        }
        uint8 c = a * b ;
        assert(c /a ==  b );
        return c;
    }
    function div(uint256 a , uint256 b ) internal returns( uint256 ){
        uint256 c = a / b ;
        return c;
    }
    function sub(uint256 a , uint256 b ) internal returns( uint256 ){
        assert(b<=a);
        return a - b;
    }
    function add(uint256 a , uint256 b ) internal returns( uint256 ){
        uint256 c = a + b;
        assert(c >= a);
        return c;
    }
}

contract SupplyChain {
    using SafeMath for uint256;
    enum Actor{Others, Producer, Retailer, Customer}
    struct User {
        bytes32 ID;
        bytes32 name;
        Actor actor;
    }

    struct Commodity {
        uint256 price;
        bytes32 commodityID;
        bytes32 commodityName;
        uint produceTime;
        bytes32 producerName;
        uint[] timestamps;
        bytes32[] retailerNames;
        uint sellTime;
        bytes32 customerName;
        bool isBinding;
        address owner;
    }

    mapping(address => User) producerMap;
    mapping(address => User) retailerMap;
    mapping(address => User) customerMap;
    mapping(bytes32 => Commodity) commodityMap;
    mapping(address => bool) whiteList;
    address owner;

    modifier onlyOwner {
        if (msg.sender != owner)
            assert(false);
        _;
    }
    function SupplyChain(){
        owner = msg.sender;
        whiteList[owner] = true;
    }

    function addWhiteList(address addr){
        whiteList[addr] = true;
    }

    function newUser(bytes32 ID, bytes32 name, Actor actor) returns (bool, string){
        User user;

        if (actor == Actor.Producer) {
            user = producerMap[msg.sender];
        } else if (actor == Actor.Retailer) {
            user = retailerMap[msg.sender];
        } else if (actor == Actor.Customer) {
            user = customerMap[msg.sender];
        } else {
            return (false, "the actor is not belong");
        }

        if (user.ID != 0x0) {
            return (false, "this ID has been occupied!");
        }
        user.ID = ID;
        user.name = name;
        user.actor = actor;
        return (true, "Success");
    }

    function newProduct(bytes32 commodityID, bytes32 commodityName,
        uint timestamp, uint _price) returns (bool, bytes32){
        Commodity commodity = commodityMap[commodityID];
        if (commodity.commodityID != 0x0) {
            return (false, "The commodityID already exist!");
        }
        User user = producerMap[msg.sender];
        if (user.ID == 0x0) {
            return (false, "The producer don't exist!");
        }
        commodity.commodityID = commodityID;
        commodity.commodityName = commodityName;
        commodity.produceTime = timestamp;
        commodity.producerName = user.name;
        commodity.price = _price;
        return (true, "Success,produce a new product");
    }


    function retailerInnerTransfer(bytes32 commodityID, uint timestamp) returns (bool, string){
        Commodity commodity = commodityMap[commodityID];
        if (commodity.commodityID == 0x0) {
            return (false, "The commodityID don't exist!");
        }
        User user = retailerMap[msg.sender];
        if (user.ID == 0x0) {
            return (false, "The retailer don't exist!");
        }
        commodity.timestamps.push(timestamp);
        commodity.retailerNames.push(user.name);
        return (true, "Success");
    }

    function fromRetailerToCustomer(bytes32 commodityID, uint timestamp) returns (bool, string){
        Commodity commodity = commodityMap[commodityID];
        if (commodity.commodityID == 0x0) {
            return (false, "The commodityID don't exist!");
        }
        commodity.sellTime = timestamp;
        return (true, "Success,Has been sold");
    }

    function getCommodityRecordsByWhiteList(bytes32 commodityID) returns (bool, string,
        bytes32 producerName, uint produceTime, bytes32[] retailerNames, uint[] retailerTimes
    , bytes32 customerName, uint sellTime, uint price){
        if (!whiteList[msg.sender]) {
            return (false, "you has no access", producerName, produceTime, retailerNames, retailerTimes, customerName, commodity.sellTime, commodity.price);
        }
        Commodity commodity = commodityMap[commodityID];
        if (commodity.commodityID == 0x0) {
            return (false, "The commodityID is not exist", producerName, produceTime, retailerNames, retailerTimes, customerName, commodity.sellTime, commodity.price);
        }
        return (true, "Success", commodity.producerName, commodity.produceTime, commodity.retailerNames, commodity.timestamps, commodity.customerName, commodity.sellTime, commodity.price);
    }


    function getCommodity(bytes32 commodityID, Actor actor)  returns (bool, string,
        bytes32 producerName, uint produceTime, bytes32[] retailerNames, uint[] retailerTimes
    , bytes32 customerName, uint sellTime, uint price){
        Commodity commodity = commodityMap[commodityID];
        if (commodity.commodityID == 0x0) {
            return (false, "The commodityID is not exist", producerName, produceTime,
            retailerNames, retailerTimes, customerName, sellTime, price);
        }
        User user;
        if (actor == Actor.Producer) {
            user = producerMap[msg.sender];
        } else if (actor == Actor.Retailer) {
            user = retailerMap[msg.sender];
        } else if (actor == Actor.Customer) {
            user = customerMap[msg.sender];
        } else {
            return (false, "the actor is not belong", producerName, produceTime,
            retailerNames, retailerTimes, customerName, sellTime, price);
        }
        if (commodity.isBinding) {
            if (commodity.owner != msg.sender) {
                return (false, "warning,this commodity has been bound", producerName, produceTime,
                retailerNames, retailerTimes, customerName, sellTime, price);
            }
        }
        if (commodity.sellTime > 0) {
            commodity.isBinding = true;
            commodity.owner = msg.sender;
            commodity.customerName = user.name;
        }
        return (true, "Success", commodity.producerName, commodity.produceTime, commodity.retailerNames, commodity.timestamps, commodity.customerName, commodity.sellTime, commodity.price);
    }

    function getTotalPrice(bytes32[] commodityIDs)  returns (bool, string, uint256 totalPrice ){
        if (!whiteList[msg.sender]) {
            return (false, "you has no access", totalPrice);
        }

        for(uint8 i =  0; i < commodityIDs.length ; i++ ){
            Commodity commodity = commodityMap[commodityIDs[i]];
            if (commodity.commodityID == 0x0) {
                return (false, "The commodityID is not exist", totalPrice);
            }else{
                totalPrice.add(commodity.price);
            }
        }
        return (true, "get the total price ", totalPrice);
    }
}
