pragma solidity ^0.4.10;
contract Test {
    function testOverflow(uint8 _value1, uint8 _value2 ) returns(uint8) {
        uint8 ret ;
        ret = _value1 * _value2;
        return ret;
    }
}
