package cn.hyperchain.application.common.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author sunligang
 * @date 2018/07/05
 */
public class PropertiesUtils {
    public static Properties getInstance(String path) {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(path);
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
