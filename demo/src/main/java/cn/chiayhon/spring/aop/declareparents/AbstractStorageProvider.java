package cn.chiayhon.spring.aop.declareparents;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractStorageProvider
        implements StorageProvider {

    @Override
    public String upload(byte[] fileBytes) {
        log.info(getType().getDesc() + "上传中...");
        return null;
    }

    @Override
    public byte[] download(String filePath) {
        log.info(getType().getDesc() + "下载中...");
        return
                new byte[0];
    }

    @Override
    public int getOrder() {
        return getType().getOrder();
    }

}
