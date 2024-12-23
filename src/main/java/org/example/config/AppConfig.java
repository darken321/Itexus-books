package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * Конфигурационный класс Spring для настройки источника данных и JdbcTemplate.
 * Этот класс используется для определения бинов и настройки компонентов приложения.
 */
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.example")
@PropertySource({"classpath:/color.properties"
        , "classpath:/application.properties"
        , "classpath:/application-sql.properties"})
@EnableAspectJAutoProxy
public class AppConfig {

    private final Environment env;

    /**
     * Создает и настраивает бин {@link MessageSource} для интернационализации.
     * Используется для загрузки сообщений из файлов ресурсов, таких как
     * messages.properties и errors.properties, с поддержкой различных локалей.
     *
     * @return настроенный {@link MessageSource}
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages", "errors");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Создает и настраивает бин {@link DataSource} для подключения к базе данных PostgreSQL.
     *
     * @return настроенный {@link DataSource}
     */

    @Bean
    public DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getProperty("spring.datasource.password"));

        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setConnectionTimeout(30000);

        return new HikariDataSource(hikariConfig);
    }

    /**
     * Создает и настраивает бин {@link JdbcTemplate} с использованием указанного {@link DataSource}.
     *
     * @param dataSource источник данных, который будет использоваться для настройки JdbcTemplate
     * @return настроенный {@link JdbcTemplate}
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}