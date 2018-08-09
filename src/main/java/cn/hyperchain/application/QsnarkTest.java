package cn.hyperchain.application;

import cn.hyperchain.application.common.utils.Logger;
import cn.qsnark.sdk.exception.TxException;
import cn.qsnark.sdk.rpc.QsnarkAPI;
import cn.qsnark.sdk.rpc.function.FuncParamReal;
import cn.qsnark.sdk.rpc.function.FunctionDecode;
import cn.qsnark.sdk.rpc.returns.*;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * @author sunligang
 * @date 2018/07/05
 */
public class QsnarkTest {
    private static QsnarkAPI api = new QsnarkAPI();

    private static Logger logger = Logger.Builder.getLogger(QsnarkTest.class);

    /**
     * 从开发者平台获取token，当然也可以通过sdk api获取
     */
    private final static String ACCESS_TOKEN = "RHMW6K5MNZGYFFL13MKJVQ";
    /**
     * hyperchain 账户地址
     */
    private final static String ADDRESS = "0xa06781871b39a22e0b0576a0eda9ceffda0560e3";
    private final static String BIN = "0x60606040526000805463ffffffff19169055341561001957fe5b5b610101806100296000396000f300606060405263ffffffff60e060020a6000350416633ad14af381146034578063569c5f6d146052578063d09de08a146078575bfe5b3415603b57fe5b605063ffffffff600435811690602435166087565b005b3415605957fe5b605f60a9565b6040805163ffffffff9092168252519081900360200190f35b3415607f57fe5b605060b6565b005b6000805463ffffffff808216850184011663ffffffff199091161790555b5050565b60005463ffffffff165b90565b6000805463ffffffff8082166001011663ffffffff199091161790555b5600a165627a7a723058205196f5c898c244d3ada034d11893c7a5d67acac307f8e5db125810804cf7bb690029";
    private final static String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"num1\",\"type\":\"uint32\"},{\"name\":\"num2\",\"type\":\"uint32\"}],\"name\":\"add\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"getSum\",\"outputs\":[{\"name\":\"\",\"type\":\"uint32\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"increment\",\"outputs\":[],\"payable\":false,\"type\":\"function\"}]";
    private final static String CONTRACT_ADDRESS = "0xc878e596ecf53e9d761ff0664ea126e035644f66";

    /**
     * 创建账户
     */
    @Test
    public void createAccount() throws Exception {
        CreteAccountReturn creteAccountReturn =
                api.createAccount(ACCESS_TOKEN);
        logger.info(creteAccountReturn.getCode(),
                creteAccountReturn.getAddress(),
                creteAccountReturn.getId(),
                creteAccountReturn.getStatus(),
                creteAccountReturn.getTime());
    }


    @Test
    public void compile() throws IOException {
        String s = "contract Accumulator{    uint32 sum = 0;   function increment(){         sum = sum + 1;     }      function getSum() returns(uint32){         return sum;     }   function add(uint32 num1,uint32 num2) {         sum = sum+num1+num2;     } }";
        CompileReturn compileReturn = api.compileContract(ACCESS_TOKEN, s);
    }

    /**
     * 合约部署
     */
    @Test
    public void deploy() throws IOException, InterruptedException {
        GetTxReciptReturn getTxReciptReturn = api.deploysyncContract(
                ACCESS_TOKEN,
                BIN,
                ADDRESS
        );
        logger.info(getTxReciptReturn);
    }


    /**
     * 合约维护
     */
    @Test
    public void maintainContract() throws IOException {
        //升级合约
        MainTainReturn upgradeMainTainReturn = api.maintainContract(ACCESS_TOKEN, ADDRESS, 1, BIN, CONTRACT_ADDRESS);
        //冻结合约
        MainTainReturn freezeMainTainReturn = api.maintainContract(ACCESS_TOKEN, ADDRESS, 2, BIN, CONTRACT_ADDRESS);
        //解冻合约
        MainTainReturn unFreezeMainTainReturn = api.maintainContract(ACCESS_TOKEN, ADDRESS, 3, BIN, CONTRACT_ADDRESS);
    }


    /**
     * 调用
     */
    @Test
    public void invoke() throws TxException, InterruptedException, IOException {
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
    }

    @Test
    public void invoke2() throws TxException, InterruptedException, IOException {
        GetTxReciptReturn getTxReciptReturn = api.invokesyncContract(
                ACCESS_TOKEN,
                false,
                ADDRESS,
                CONTRACT_ADDRESS,
                ABI,
                "getSum"
        );
        logger.info(getTxReciptReturn.getRet());
        System.out.println(FunctionDecode.resultDecode("getSum", ABI, "0x00000000000000000000000000000000000000000000000000000000000001c8"));
    }


    @Test
    public void hexToString() throws UnsupportedEncodingException {
        String strDecoded = new String(ByteUtils.fromHexString("1a7468697320494420686173206265656e206f6363757069656421"), "UTF-8");
        logger.info(strDecoded);
    }





}
