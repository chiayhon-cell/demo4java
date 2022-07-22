package cn.chiayhon.enumerate;

import lombok.Getter;

@Getter
public enum DatabaseType {

    MYSQL("Mysql", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://{}:{}/{}?serverTimezone=GMT%2B8&useUnicode=true&useSSL=false&characterEncoding=utf-8&allowPublicKeyRetrieval=true");

    private final String name;

    private final String driverClass;

    private final String urlPattern;

    DatabaseType(String name, String driverClass, String urlPattern) {
        this.name = name;
        this.driverClass = driverClass;
        this.urlPattern = urlPattern;
    }
}
