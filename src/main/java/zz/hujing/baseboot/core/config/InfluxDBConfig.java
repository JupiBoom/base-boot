package zz.hujing.baseboot.core.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.username}")
    private String username;

    @Value("${influxdb.password}")
    private String password;

    @Value("${influxdb.database}")
    private String database;

    @Bean
    public InfluxDB influxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);
        if (!influxDB.databaseExists(database)) {
            influxDB.createDatabase(database);
        }
        influxDB.setDatabase(database);
        return influxDB;
    }
}