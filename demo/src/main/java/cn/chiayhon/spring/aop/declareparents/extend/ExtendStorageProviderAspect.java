package cn.chiayhon.spring.aop.declareparents.extend;

import cn.chiayhon.spring.aop.declareparents.StorageType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * 存储服务功能扩展切面
 */
@Aspect
@Component
@Slf4j
public class ExtendStorageProviderAspect {

    @DeclareParents(value = "cn.chiayhon.spring.aop.declareparents.AliStorageProvider", defaultImpl = ExtendAliStorageProviderProvider.class)
    static ExtendStorageProvider ali;

    @DeclareParents(value = "cn.chiayhon.spring.aop.declareparents.TencentStorageProvider", defaultImpl = ExtendTencentStorageProviderProvider.class)
    static ExtendStorageProvider tencent;

//    @DeclareParents(value = "cn.chiayhon.spring.aop.declareparents.AzureStorageProvider", defaultImpl = ExtendAzureStorageProviderProvider.class)
//    static ExtendStorageProvider azure;

    // 匹配阿里云存储服务的上传方法，并在调用该方法前进行通知
    @Before(value = "bean(aliStorageProvider) && (execution(* upload(..)) ) && this(aliyun)", argNames = "joinPoint,aliyun")
    public void beforeExtendAliStorage(JoinPoint joinPoint, ExtendStorageProvider aliyun) {
        log.info("这是阿里云存储扩展器");
    }

    /**
     * 存储服务功能扩展默认实现
     */
    @Slf4j
    public static class ExtendAliStorageProviderProvider
            extends AbstractExtendStorageProviderProvider {

        @Override
        public StorageType getType() {
            return StorageType.ALI;
        }
    }

    /**
     * 存储服务功能扩展默认实现
     */
    @Slf4j
    public static class ExtendAzureStorageProviderProvider
            extends AbstractExtendStorageProviderProvider {
        @Override
        public StorageType getType() {
            return StorageType.AZURE;
        }
    }

    /**
     * 存储服务功能扩展默认实现
     */
    @Slf4j
    public static class ExtendTencentStorageProviderProvider
            extends AbstractExtendStorageProviderProvider {


        @Override
        public StorageType getType() {
            return StorageType.TENCENT;
        }
    }

    /**
     * 存储服务功能扩展默认实现
     */
    @Slf4j
    public static abstract class AbstractExtendStorageProviderProvider
            implements ExtendStorageProvider {

        @Override
        public boolean exist(String filePath) {
            if ((System.currentTimeMillis() & 1) == 1) {
                log.info("在{}查找文件：文件[{}]存在", getType().getDesc(), filePath);
                return true;
            }
            log.info("在{}查找文件：文件[{}]不存在", getType().getDesc(), filePath);
            return false;
        }

        @Override
        public boolean delete(String filePath) {
            if ((System.currentTimeMillis() & 1) == 1) {
                log.info("在{}删除文件：文件[{}]删除成功", getType().getDesc(), filePath);
                return true;
            }
            log.info("在{}删除文件：文件[{}]删除失败", getType().getDesc(), filePath);
            return false;
        }

    }


}
