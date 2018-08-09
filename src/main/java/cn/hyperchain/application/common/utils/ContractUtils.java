package cn.hyperchain.application.common.utils;

import cn.qsnark.sdk.rpc.QsnarkAPI;
import cn.qsnark.sdk.rpc.returns.CompileReturn;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Properties;

/**
 * solidity合约工具
 *
 * @author sunligang
 * @date 2018/06/14
 */
public class ContractUtils {

    private static Logger logger = Logger.Builder.getLogger(ContractUtils.class);

    private static QsnarkAPI api = new QsnarkAPI();
    private static Properties properties = PropertiesUtils.getInstance("hyperchain.properties");
    private static String accessToken;
    private static String address;
    private static String abi;
    private static String bin;
    private static String contractAddress;

    static {
        //从配置文件中读取token
        accessToken = properties.getProperty("access_token");
        // abi
        abi = properties.getProperty("abi");
        //合约地址
        contractAddress = properties.getProperty("contract_address");
        // 账户地址
        address = properties.getProperty("address");
    }


    /**
     * 单个sol合约文件的编译和部署，并且合约文件需要位于/resource/contractSourceCode下
     *
     * @return 编译结果json
     */
    public static boolean compileAndDeploy(String token) {
        //合约文件夹
        String contractFolder = ContractUtils.class.getResource("/contractSourceCode").getPath();
        File folder = new File(contractFolder);
        String contractFile = "";
        File[] files = null;
        if (folder.isDirectory()) {
            files = folder.listFiles();
            assert files != null;
            if (files.length > 0) {
                contractFile = files[0].getPath();
            }
        }

        String contractContent = readFromFile(contractFile);
        //编译合约
        CompileReturn compile;
        try {
            compile = api.compileContract(token, contractContent);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        bin = compile.getCts_bin();

        abi = compile.getCts_bin();


        try {
            contractAddress = api.deploysyncContract(accessToken,
                    bin,
                    address
            ).getContract_address();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //将bin和abi和合约地址保存到配置的文件中
        writeIntoFile(bin, properties.getProperty("bin_path"));
        writeIntoFile(abi, properties.getProperty("abi_path"));
        writeIntoFile(contractAddress, properties.getProperty("contract_address_path"));
        return true;
    }

    /**
     * 根据路径读取文件
     *
     * @param path 文件路径
     * @return
     */
    private static String readFromFile(String path) {
        File file = new File(path);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[128];
        int len;
        try {
            while ((len = fileReader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 将string写入到目的文件
     *
     * @param strSource
     * @param destPath
     */
    private synchronized static void writeIntoFile(String strSource, String destPath) {
        // 如果不存在则创建
        File file = new File(destPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileOutputStream(destPath).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byte[] info = strSource.getBytes();
        int index = 0;
        while (index < info.length) {
            byteBuffer.put(info, index, Math.min(info.length - index, 128));
            index += 128;
            byteBuffer.flip();
            try {
                fileChannel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteBuffer.clear();
        }
    }

    public static String getAbi() {
        return abi;
    }


    public static String getContractAddress() {
        return contractAddress;
    }

    /**
     * 获取账户地址
     *
     * @return
     */
    public static String getAddress() {
        return address;
    }

}
