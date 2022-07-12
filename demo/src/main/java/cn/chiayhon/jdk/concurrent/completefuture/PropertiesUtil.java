package cn.chiayhon.jdk.concurrent.completefuture;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    public static final String FILE_PATH = "./test.properties";

    //通过传入的路径及key，获得对应的值
    public static String getValue(String path, String key) {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("File Read Failed...", e);
        }
        return properties.getProperty(key);
    }

    //通过key直接获取对应的值
    public static String getValue(String key) {
        Properties properties = new Properties();
        try {
            final ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
            properties.load(classLoader.getResourceAsStream(FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("File Read Failed...", e);
        }
        return properties.getProperty(key);
    }

}