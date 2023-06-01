package cn.chiayhon.spring.aop.declareparents.extend;

import cn.chiayhon.spring.aop.declareparents.StorageProvider;
import cn.chiayhon.spring.aop.declareparents.StorageType;

import java.util.Objects;
import java.util.Optional;

/**
 * 存储服务提供
 */
public interface ExtendStorageProvider{

    static Optional<ExtendStorageProvider> convertToExtend(StorageProvider provider){
        if (provider instanceof  ExtendStorageProvider){
            return Optional.of((ExtendStorageProvider)provider);
        }
        return Optional.empty();
    }

    /**
     * 确认文件是否存在
     * @param filePath
     * @return
     */
    boolean exist(String filePath);


    /**
     * 删除文件
     * @param filePath
     * @return
     */
    boolean delete(String filePath);

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
        if (type == null){
            return false;
        }
        return Objects.equals(getType(), type);
    }


}
