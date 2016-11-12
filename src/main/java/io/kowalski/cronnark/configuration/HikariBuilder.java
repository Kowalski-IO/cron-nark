package io.kowalski.cronnark.configuration;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HikariBuilder implements Serializable {

    private static final long serialVersionUID = -6970282022245016525L;

    private String poolName;
    private String jdbcUrl;
    private String username;
    private String password;
    private Map<String, String> dataSourceProperties;

    public HikariDataSource buildDatasource() {
        final HikariConfig config = new HikariConfig();
        config.setPoolName(poolName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        if (jdbcUrl.contains("sqlserver")) {
            config.setConnectionTestQuery("SELECT GETDATE()");
        }

        if (dataSourceProperties != null && !dataSourceProperties.isEmpty()) {
            for (final Entry<String, String> dataSourceProperty : dataSourceProperties.entrySet()) {
                config.addDataSourceProperty(dataSourceProperty.getKey(), dataSourceProperty.getValue());
            }
        }

        return new HikariDataSource(config);
    }

}
