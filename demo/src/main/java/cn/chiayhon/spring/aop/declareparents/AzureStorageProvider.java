package cn.chiayhon.spring.aop.declareparents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微软Azure存储
 */
@Slf4j
@Component
public class AzureStorageProvider extends AbstractStorageProvider{

    @Override
    public StorageType getType() {
        return StorageType.AZURE;
    }

}
