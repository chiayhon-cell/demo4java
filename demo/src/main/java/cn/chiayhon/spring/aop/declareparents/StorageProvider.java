package cn.chiayhon.spring.aop.declareparents;

import org.springframework.core.Ordered;

import java.util.Objects;

/**
 * 存储服务提供
 */
public interface StorageProvider extends Ordered {

    /**
     * 上传文件
     *
     * @param fileBytes
     * @return
     */
    String upload(byte[] fileBytes);

    /**
     * 下载文件
     *
     * @param filePath
     * @return
     */
    byte[] download(String filePath);

    /**
     * 获取存储类型
     *
     * @return
     */
    StorageType getType();

    /**
     * 是否支持该类型
     *
     * @param type
     * @return
     */
    default boolean support(StorageType type) {
        return Objects.equals(getType(), type);
    }

}
