package org.example.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;


/**
 * Конфигурационный класс Spring для настройки источника данных и JdbcTemplate.
 * Этот класс используется для определения бинов и настройки компонентов приложения.
 */
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.example")
@PropertySource({"classpath:/color.properties"
        , "classpath:/application.properties"
        , "classpath:/hibernate.properties"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
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
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    private Properties getProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        hibernateProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
        return hibernateProperties;
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) throws IOException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // пакеты, содержащие сущности
        sessionFactoryBean.setPackagesToScan("org.example.model");
        //устанавливаем свойства
        sessionFactoryBean.setHibernateProperties(getProperties());
        //завершение настройки и инициализация бина
        sessionFactoryBean.afterPropertiesSet();
        //извлечение объекта типа SessionFactory
        return sessionFactoryBean.getObject();
    }
}