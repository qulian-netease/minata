package cn.hyperchain.application.contract.invoke;

import cn.hyperchain.application.common.constant.Code;
import cn.hyperchain.application.common.response.BaseResult;
import cn.hyperchain.application.common.response.BaseResultFactory;
import cn.hyperchain.application.common.utils.ContractUtils;
import cn.hyperchain.application.contract.business.Actor;
import cn.qsnark.sdk.exception.TxException;
import cn.qsnark.sdk.rpc.QsnarkAPI;
import cn.qsnark.sdk.rpc.function.FuncParamReal;
import cn.qsnark.sdk.rpc.function.FunctionDecode;
import cn.qsnark.sdk.rpc.returns.GetTxReciptReturn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * @author sunligang
 * @date 2018/07/05
 */
@Component
public class ContractInvoke {

    private static QsnarkAPI api = new QsnarkAPI();

    private final static String FLAG_OK = "ok";

    /**
     * 对应于合约的newUser方法
     *
     * @return
     */
    public BaseResult newUser(String token, String invokeAddress, String ID, String name, Actor actor) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[3];
        arrFunParamReal[0] = new FuncParamReal("bytes32", ID);
        arrFunParamReal[1] = new FuncParamReal("bytes32", name);
        //enum对应于string
        switch (actor) {
            case Producer:
                arrFunParamReal[2] = new FuncParamReal("uint8", 1);
                break;
            case Retailer:
                arrFunParamReal[2] = new FuncParamReal("uint8", 2);
                break;
            case Customer:
                arrFunParamReal[2] = new FuncParamReal("uint8", 3);
                break;
            case Others:
                arrFunParamReal[2] = new FuncParamReal("uint8", 0);
                break;
            default:
                break;
        }
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "newUser",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("newUser", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("newUser", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }


    /**
     * 添加白名单
     *
     * @return
     */
    public BaseResult addWhiteList(String token, String invokeAddress, String address) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[1];
        arrFunParamReal[0] = new FuncParamReal("address", address);
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "addWhiteList",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("addWhiteList", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("addWhiteList", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }


    /**
     * 对应于合约的newProduct方法
     *
     * @return
     */
    public BaseResult newProduct(String token, String invokeAddress, String commodityID, String commodityName, int price ) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[4];
        arrFunParamReal[0] = new FuncParamReal("bytes32", commodityID);
        arrFunParamReal[1] = new FuncParamReal("bytes32", commodityName);
        arrFunParamReal[2] = new FuncParamReal("uint256", System.currentTimeMillis());
        arrFunParamReal[3] = new FuncParamReal("uint256", price);
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "newProduct",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("newProduct", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("newProduct", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }


    /**
     * 对应于合约的retailerInnerTransfer方法
     *
     * @return
     */
    public BaseResult retailerInnerTransfer(String token, String invokeAddress, String commodityID) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[2];
        arrFunParamReal[0] = new FuncParamReal("bytes32", commodityID);
        arrFunParamReal[1] = new FuncParamReal("uint256", System.currentTimeMillis());
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "retailerInnerTransfer",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("retailerInnerTransfer", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("retailerInnerTransfer", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }


    /**
     * 对应于合约的fromRetailerToCustomer方法
     *
     * @return
     */
    public BaseResult fromRetailerToCustomer(String token, String invokeAddress, String commodityID) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[2];
        arrFunParamReal[0] = new FuncParamReal("bytes32", commodityID);
        arrFunParamReal[1] = new FuncParamReal("uint256", System.currentTimeMillis());
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "fromRetailerToCustomer",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("fromRetailerToCustomer", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("fromRetailerToCustomer", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }

    /**
     * 对应于合约的getCommodityRecordsByWhiteList方法
     *
     * @return
     */
    public BaseResult getCommodityRecordsByWhiteList(String token, String invokeAddress, String commodityID) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[1];
        arrFunParamReal[0] = new FuncParamReal("bytes32", commodityID);
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "getCommodityRecordsByWhiteList",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("getCommodityRecordsByWhiteList", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("getCommodityRecordsByWhiteList", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }

    /**
     * 对应于合约的getCommodity方法
     *
     * @return
     */
    public BaseResult getCommodity(String token, String invokeAddress, String commodityID, Actor actor) {
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[2];
        arrFunParamReal[0] = new FuncParamReal("bytes32", commodityID);
        //enum对应于string
        switch (actor) {
            case Producer:
                arrFunParamReal[1] = new FuncParamReal("uint8", 1);
                break;
            case Retailer:
                arrFunParamReal[1] = new FuncParamReal("uint8", 2);
                break;
            case Customer:
                arrFunParamReal[1] = new FuncParamReal("uint8", 3);
                break;
            case Others:
                arrFunParamReal[1] = new FuncParamReal("uint8", 0);
                break;
            default:
                break;
        }
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "getCommodity",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("getCommodity", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("getCommodity", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }

    /**
     * 监管部门（白名单）查询一批商品的价格
     * @param commodityIDs 商品id们
     * @return
     */
    public BaseResult getTotalPrice(String token, String invokeAddress, List<String> commodityIDs){
        //构造参数
        FuncParamReal[] arrFunParamReal = new FuncParamReal[1];
        arrFunParamReal[0] = new FuncParamReal("bytes32[]", commodityIDs);
        GetTxReciptReturn getTxReciptReturn = null;
        try {
            getTxReciptReturn = api.invokesyncContract(
                    token,
                    false,
                    invokeAddress,
                    ContractUtils.getContractAddress(),
                    ContractUtils.getAbi(),
                    "getTotalPrice",
                    arrFunParamReal
            );
        } catch (IOException | TxException | InterruptedException e) {
            e.printStackTrace();
        }

        BaseResult baseResult = null;

        if (FLAG_OK.equals(getTxReciptReturn.getStatus())) {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.SUCCESS,
                        FunctionDecode.resultDecode("getTotalPrice", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                baseResult = BaseResultFactory.produceResult(
                        Code.INVOKE_FAIL,
                        FunctionDecode.resultDecode("getTotalPrice", ContractUtils.getAbi(), getTxReciptReturn.getRet()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return baseResult;
    }
}
