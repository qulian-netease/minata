# minata
基于开发者平台的供应链溯源案例（网易云课程）
## 环境准备
### 注册与授权
1. 使用手机号进行注册后
2. 进入控制台
3. 创建应用
![创建应用](/Users/sunligang/Documents/笔记博客图片资源/创建应用.png)
4. 获取AppKey和AppSecret
![应用信息](/Users/sunligang/Documents/笔记博客图片资源/应用详情.png)
5. 获取token
使用我们的注册的账户和密码包括刚刚的appkey和appsecret来获取token
在这里我们使用的是开发者平台提供的sdk
```java
GetTokenReturn getTokenReturn = api.getAccess_Token(
             "AppKey",
             "AppSecret",
             "phone",
             "password");
     logger.info(getTokenReturn.getCode(),
             getTokenReturn.getAccess_token(),
             getTokenReturn.getRefresh_token());
```

### 合约的编写

#### 业务分析
当前的业务是溯源案例，即供应链溯源。所谓的供应链溯源实则是将商品从原料到买家手中的一些列的行为给记录到区块链上，利用区块链的不可篡改，去中心化这样的特点来保证整个一溯源流程的安全可信。
#### 场景描述
供应链溯源合约有以下几类参与方：

- 商品厂商：保存于mapping(address => User) producerMap
- 各级经销商：保存于mapping(address => User) retailerMap
- 消费者：保存于mapping(address => User) customerMap
各类参与方均通过newUser方法进行**上链**登记。通过传递不同的Actor值来指定不同参与方。

厂商首先通过newProduct方法将出厂商品登记到区块链，随后商品分销到下一级经销商，接下来的环节，每一次商品的分销均在接收商品的经销商处调用retailerInnerTransfer方法将商品进行上链登记，最终商品在零售商处由消费者购买，调用fromRetailerToCustomer方法进行最终登记，完成整个出厂-多级分销-零售的流程。商品一旦由厂商登记上链，便可通过getCommodity查询到商品当前的分销信息，只有处于该分销路径上的参与方才允许查询。此外，通过addWhiteList可以为指定参与方添加顶级查询权限，被添加到WhiteList的参与方，即使不参与到某商品的分销路径中，也可查询到该商品的分销信息。
#### 合约代码
```js
contract SupplyChain {
    enum Actor{ Others, Producer, Retailer, Customer}
    enum Commoditytype{ Wine, Jewelry, Shrimp, Others}
    enum Phase{ Supplier, Producer, Dealer, Retailer, Customer}

    struct User{
        bytes32 ID;
        bytes32 name;
        Actor actor;
    }

    struct Commodity{
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

    function SupplyChain(){
        owner = msg.sender;
        whiteList[owner] = true;
    }

    function addWhiteList(address addr){
        whiteList[addr] = true;
    }

    function newUser(bytes32 ID, bytes32 name,Actor actor) returns(bool, string){
        User user;

        if(actor == Actor.Producer){
            user = producerMap[msg.sender];
        }else if(actor == Actor.Retailer){
            user = retailerMap[msg.sender];
        }else if(actor == Actor.Customer){
            user = customerMap[msg.sender];
        }else{
            return (false,"the actor is not belong");
        }

        if(user.ID != 0x0){
            return (false, "this ID has been occupied!");
        }
        user.ID = ID;
        user.name = name;
        user.actor = actor;
        return (true, "Success");
    }

    // this interface just for producer
    function newProduct(bytes32 commodityID, bytes32 commodityName,
    uint timestamp) returns(bool,bytes32){
        Commodity commodity = commodityMap[commodityID];
        if(commodity.commodityID != 0x0) {
            return (false,"The commodityID already exist!");
        }
        User user = producerMap[msg.sender];
        if(user.ID == 0x0) {
            return (false,"The producer don't exist!");
        }
        commodity.commodityID = commodityID;
        commodity.commodityName = commodityName;
        commodity.produceTime = timestamp;
        commodity.producerName = user.name;
        return (true,"Success,produce a new product");
    }


    // this interface just for retailer
    function retailerInnerTransfer(bytes32 commodityID,uint timestamp) returns(bool, string){
        Commodity commodity = commodityMap[commodityID];
        if(commodity.commodityID == 0x0) {
            return (false,"The commodityID don't exist!");
        }
        User user = retailerMap[msg.sender];
        if(user.ID == 0x0) {
            return (false,"The retailer don't exist!");
        }
        commodity.timestamps.push(timestamp);
        commodity.retailerNames.push( user.name );
        return (true,"Success");
    }

    function fromRetailerToCustomer(bytes32 commodityID,uint timestamp) returns(bool, string){
        Commodity commodity = commodityMap[commodityID];
        if(commodity.commodityID == 0x0) {
            return (false,"The commodityID don't exist!");
        }
        commodity.sellTime = timestamp;
        return (true,"Success,Has been sold");
    }

    // just for Supervision organization
    function getCommodityRecordsByWhiteList(bytes32 commodityID) returns(bool,string,
    bytes32 producerName,uint produceTime, bytes32[] retailerNames,uint[] retailerTimes
    , bytes32 customerName,uint sellTime){
        if(!whiteList[msg.sender]){
            return (false,"you has no access",producerName, produceTime, retailerNames, retailerTimes, customerName,commodity.sellTime);
        }
        Commodity commodity = commodityMap[commodityID];
        if(commodity.commodityID == 0x0){
            return (false,"The commodityID is not exist",producerName, produceTime, retailerNames, retailerTimes,customerName,commodity.sellTime);
        }
        return (true,"Success",commodity.producerName, commodity.produceTime, commodity.retailerNames, commodity.timestamps, commodity.customerName,commodity.sellTime);
    }


    function getCommodity(bytes32 commodityID,Actor actor)  returns(bool,string,
    bytes32 producerName,uint produceTime,bytes32[] retailerNames,uint[] retailerTimes
    , bytes32 customerName,uint sellTime){
        Commodity commodity = commodityMap[commodityID];
        if(commodity.commodityID == 0x0){
            return (false,"The commodityID is not exist",producerName,produceTime,
            retailerNames,retailerTimes,customerName,sellTime);
        }
        User user;
        if(actor == Actor.Producer){
            user = producerMap[msg.sender];
        }else if(actor == Actor.Retailer){
            user = retailerMap[msg.sender];
        }else if(actor == Actor.Customer){
            user = customerMap[msg.sender];
        }else{
            return (false,"the actor is not belong",producerName,produceTime,
            retailerNames,retailerTimes,customerName,sellTime);
        }
        if(commodity.isBinding){
            if(commodity.owner != msg.sender){
                return (false,"warning,this commodity has been bound",producerName,produceTime,
            retailerNames,retailerTimes,customerName,sellTime);
            }else{
                (false,"has already bind",commodity.producerName,commodity.retailerNames,commodity.customerName);
            }
        }
        if(commodity.sellTime > 0 ) {
            commodity.isBinding = true;
            commodity.owner = msg.sender;
            commodity.customerName = user.name;
        }
        return (true,"Success",commodity.producerName,commodity.produceTime,commodity.retailerNames,commodity.timestamps,commodity.customerName,commodity.sellTime);
    }
}
```
### 在使用sdk进行编译和部署
![编译部署](/Users/sunligang/Documents/笔记博客图片资源/编译部署流程.png)
#### 对应的hpyerchain.properties的配置
由于在sdk上进行编译和部署则需要持久化编译产生的abi和bin已经部署后获得的合约地址
于是我们在配置文件中配置相关的文件去保存这些内容
**文件内容**

```shell
access_token=WVDSRC22PTGS3H4GMFQHGA
# refresh_token=8ZEPJTSRV2CRCGMZHG4MPA
hyperchain_user_address=0xc18b43373fc5ac8effbb5871937056e4bb3aa0ab
# 是否重新编译和部署合约
redeploy=true
# 合约地址文件
contract_address_path=contractAddress
# Abi文件
bin_path=BIN
# ABI文件
abi_path=ABI
```

- 编译
使用sdk在编译时需要携带token，token存在7200秒的有效时间，超出了时间需要再次获取
```java
//合约内容，一般会从合约文件中读取
String contractContent = "
  contract Accumulator {
      uint32 sum = 0;
      function increment() {
          sum = sum + 1;
      }
      function getSum() returns(uint32) {
          return sum;
      }
      function add(uint32 num1,uint32 num2) {
        sum = sum+num1+num2;
      }
  }
";
ABI
```json
[{
	"constant": false,
	"inputs": [{
		"name": "num1",
		"type": "uint32"
	}, {
		"name": "num2",
		"type": "uint32"
	}],
	"name": "add",
	"outputs": [],
	"payable": false,
	"type": "function"
}, {
	"constant": false,
	"inputs": [],
	"name": "getSum",
	"outputs": [{
		"name": "",
		"type": "uint32"
	}],
	"payable": false,
	"type": "function"
}, {
	"constant": false,
	"inputs": [],
	"name": "increment",
	"outputs": [],
	"payable": false,
	"type": "function"
}]
//传入有效的token 进行调用            
CompileReturn compileReturn = api.compileContract(ACCESS_TOKEN, contractConten);
```
- 部署
```java
//部署合约并输出合约地址 需要传入 hyperchain账户地址
DeployConReturn deployConReturn = api.deployContract(ACCESS_TOKEN,
                BIN,
                ADDRESS,
                (DevCallback) address -> System.out.println(address));
```

### 在开发者平台网页上进行部署合约
![合约部署](/Users/sunligang/Documents/笔记博客图片资源/合约部署.png)
#### 对应的hyperchain配置
由于已经部署好了合约则只需闯入abi和合约地址
```shell
access_token=WVDSRC22PTGS3H4GMFQHGA
# refresh_token=8ZEPJTSRV2CRCGMZHG4MPA
hyperchain_user_address=0xc18b43373fc5ac8effbb5871937056e4bb3aa0ab
# 是否重新编译和部署合约
redeploy=false
####已有部署好的合约
#合约地址
contract_address=0x07c3c7c4cc7820a169964b1fa5a951b94da618d7
# abi（如果通过代码部署则不填写）
abi=[{"constant":false,"inputs":[{"name":"num1","type":"uint32"},{"name":"num2","type":"uint32"}],"name":"add","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"getSum","outputs":[{"name":"","type":"uint32"}],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"increment","outputs":[],"payable":false,"type":"function"}]
```
### 调用合约函数
```java
FuncParamReal funcParamReal1 = new FuncParamReal("uint32", 456);
FuncParamReal funcParamReal2 = new FuncParamReal("uint32", 0);
api.invokeContract(
       ACCESS_TOKEN,
       false,
       ADDRESS,
       CONTRACT_ADDRESS,
       ABI,
       list -> {
           System.out.println(list);
       },
       "add",
       funcParamReal1,
       funcParamReal2
);
```
