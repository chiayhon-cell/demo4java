package cn.chiayhon.rest.service;

import cn.chiayhon.properties.DatabaseProperties;

import java.io.InputStream;

/**
 * 数据库管理类，基于{@link DatabaseAPIService}提供的方法实现对应功能的复合操作类
 */
public interface DatabaseManageService {

    /**
     * 从指定流中初始化数据库
     *
     * @param databaseProperties
     * @param inputStream
     * @return
     */
    boolean initDatabase(DatabaseProperties databaseProperties, InputStream inputStream);
}
