package cn.chiayhon.spring.aop.declareparents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 腾讯云存储
 */
@Slf4j
@Component
public class TencentStorageProvider
        extends AbstractStorageProvider {

    @Override
    public StorageType getType() {
        return StorageType.TENCENT;
    }

}
