package cn.hyperchain.application.controller;

import cn.hyperchain.application.common.response.BaseResult;
import cn.hyperchain.application.contract.business.Actor;
import cn.hyperchain.application.contract.invoke.ContractInvoke;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 合约操作
 *
 * @author sunligang
 * @date 2018/07/09
 */
@RestController
public class ContractController {

    @Autowired
    private ContractInvoke contractInvoke;


    /**
     * 添加白名单
     *
     * @param address 账户地址
     */
    @ApiOperation(value = "消费者发起的转移", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/whiteList", method = RequestMethod.POST)
    public BaseResult addWhiteList(
            @ApiParam(value = "accessToken") @RequestParam String token,
            @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
            @ApiParam(value = "账户地址") @RequestParam String address) {
        return contractInvoke.addWhiteList(token, invokeAddress, address);
    }

    /**
     * 创建一个参与方
     *
     * @param ID    参与方ID 注意需要是hyperchain账户
     * @param name  参与方姓名
     * @param actor 参与方类型
     * @return 创建用户是否成功
     */
    @ApiOperation(value = "增加一个参与方", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/user", method = RequestMethod.POST)
    public BaseResult newUser(@ApiParam(value = "accessToken") @RequestParam String token,
                              @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
                              @ApiParam(value = "参与方ID") @RequestParam String ID,
                              @ApiParam(value = "参与方name") @RequestParam String name,
                              @ApiParam(value = "参与方类型") @RequestParam Actor actor) {
        return contractInvoke.newUser(token, invokeAddress, ID, name, actor);
    }

    /**
     * 生产一件商品
     *
     * @param commodityID   商品ID
     * @param commodityName 商品name
     * @return 1.是否已经有了该商品 2.调用者是否是一个生产者 3.成功生产
     */
    @ApiOperation(value = "生产一件产品", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/product", method = RequestMethod.POST)
    public BaseResult newProduct(@ApiParam(value = "accessToken") @RequestParam String token,
                                 @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
                                 @ApiParam(value = "商品ID") @RequestParam String commodityID,
                                 @ApiParam(value = "商品name") @RequestParam String commodityName,
                                 @ApiParam(value = "商品价格") @RequestParam Integer price) {
        return contractInvoke.newProduct(token, invokeAddress, commodityID, commodityName, price);
    }


    /**
     * 经销商发起的商品转移
     *
     * @param commodityID 商品ID
     * @return 1.是否已经有了该商品 2.调用者是否是一个经销商 3.成功生产
     */
    @ApiOperation(value = "经销商发起的转移", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/retailerInnerTransfer", method = RequestMethod.POST)
    public BaseResult retailerInnerTransfer(
            @ApiParam(value = "accessToken") @RequestParam String token,
            @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
            @ApiParam(value = "商品ID") @RequestParam String commodityID) {
        return contractInvoke.retailerInnerTransfer(token, invokeAddress, commodityID);
    }


    /**
     * 消费者发起的商品转移
     *
     * @param commodityID 商品ID
     * @return 1.是否已经有了该商品 2.调用者是否是一个消费者 3.成功生产
     */
    @ApiOperation(value = "消费者发起的转移", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/fromRetailerToCustomer", method = RequestMethod.POST)
    public BaseResult fromRetailerToCustomer(@ApiParam(value = "accessToken") @RequestParam String token,
                                             @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
                                             @ApiParam(value = "商品ID") @RequestParam String commodityID) {
        return contractInvoke.fromRetailerToCustomer(token, invokeAddress, commodityID);
    }

    /**
     * 通过白名单获取商品记录
     *
     * @param commodityID 商品ID
     * @return 1.是否已经有了该商品 2.调用者是否是一个消费者 3.成功生产
     */
    @ApiOperation(value = "消费者发起的转移", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/getCommodityRecordsByWhiteList", method = RequestMethod.POST)
    public BaseResult getCommodityRecordsByWhiteList(
            @ApiParam(value = "accessToken") @RequestParam String token,
            @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
            @ApiParam(value = "商品ID") @RequestParam String commodityID) {
        return contractInvoke.getCommodityRecordsByWhiteList(token, invokeAddress, commodityID);
    }


    /**
     * 只获取当前账户的商品信息
     *
     * @param commodityID 商品ID
     * @param actor       账户类型
     * @return
     */
    @ApiOperation(value = "通过调用者账户去获取账户信息", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/getCommodity", method = RequestMethod.POST)
    public BaseResult getCommodity(
            @ApiParam(value = "accessToken") @RequestParam String token,
            @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
            @ApiParam(value = "商品ID") @RequestParam String commodityID,
            @ApiParam(value = "参与方类型") @RequestParam Actor actor) {
        return contractInvoke.getCommodity(token, invokeAddress, commodityID, actor);
    }

    /**
     * 获取商品的总价格
     *
     * @return
     */
    @ApiOperation(value = "通过商品ID列表获取总价格", notes = "防伪溯源案例")
    @RequestMapping(value = "/v1/totalPrice", method = RequestMethod.POST)
    public BaseResult getTotalPrice(
            @ApiParam(value = "accessToken") @RequestParam String token,
            @ApiParam(value = "调用者账户地址") @RequestParam String invokeAddress,
            @ApiParam(value = "商品ID列表") @RequestParam List<String> commodityIDs) {
        return contractInvoke.getTotalPrice(token, invokeAddress, commodityIDs);
    }
}
