package cn.chiayhon.spring.aop.declareparents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StorageType {

    ALI("阿里云oss存储",0),
    TENCENT("腾讯云cos存储",1),
    AZURE("微软azure存储",2);

    private final String desc;
    private final int order;
}
