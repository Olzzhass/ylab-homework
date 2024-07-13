package kaz.olzhas.ylab.config;


import kaz.olzhas.ylab.util.YamlPropertySourceFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс {@code DataSourceConfiguration} отвечает за конфигурацию источника данных,
 * используемого приложением.
 *
 * <p>Он определяет бин источника данных, который предоставляет необходимую конфигурацию
 * для подключения к базе данных. Кроме того, он может быть расширен для настройки
 * инструментов миграции баз данных, таких как SpringLiquibase.
 *
 * <p>Пример использования:
 * <pre>
 * // Создание экземпляра JdbcTemplate в вашем приложении Spring.
 * &#64;Autowired
 * private JdbcTemplate jdbcTemplate;
 * </pre>
 *
 * <p>Класс аннотирован как {@code @Configuration}, чтобы указать, что он предоставляет
 * определения бинов. Он также аннотирован как {@code @PropertySource}, чтобы указать
 * расположение источника свойств (application.yml).</p>
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class DataSourceConfiguration {

    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.driver-class-name}")
    private String driver;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${liquibase.change-log}")
    private String changeLogFile;
    @Value("${liquibase.liquibase-schema}")
    private String schemaName;

    /**
     * Создает бин {@link JdbcTemplate}, настроенный с свойствами источника данных.
     *
     * @return JdbcTemplate, настроенный с указанным источником данных.
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new JdbcTemplate(dataSource);
    }

    /**
     * Создает бин {@link SpringLiquibase} для управления миграциями базы данных.
     *
     * @return SpringLiquibase для применения изменений к базе данных.
     */
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setLiquibaseSchema(schemaName);
        liquibase.setChangeLog(changeLogFile);
        liquibase.setDataSource(jdbcTemplate().getDataSource());
        return liquibase;
    }
}