package cn.chiayhon.properties;

import cn.chiayhon.enums.DatabaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.helpers.MessageFormatter;

/**
 * 数据库配置类
 */
@Data
@AllArgsConstructor
public class DatabaseProperties {

    private DatabaseType database; // 数据库类型

    private String dataBaseName; // 数据库名

    private String host; // 主机名

    private int port; // 端口

    private String username; // 用户名

    private String password; // 密码

    /**
     * 获取url
     *
     * @return jdbcURL
     */
    public String getUrl() {
        final String urlPattern = database.getUrlPattern();
        final Object[] params = {host, port, dataBaseName};
        return MessageFormatter.arrayFormat(urlPattern, params).getMessage();
    }


}
