package org.example.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;


/**
 * Класс конфигурации Spring для приложения.
 * Этот класс используется для определения бинов и настройки компонентов приложения.
 *
 */
@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:/color.properties")
public class AppConfig {
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
}