pragma solidity ^0.4.10;

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

contract Test {
    using SafeMath for uint8;
    function testOverflow(uint8 _value1, uint8 _value2 ) returns(uint8) {
        uint8 ret ;
        ret = _value1.mul(_value2);
        return ret;
    }
}
