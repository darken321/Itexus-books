package org.example;

import org.example.config.AppConfig;
import org.example.controller.MainMenu;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Главный класс приложения для управления книгами.
 * Инициализирует контекст Spring и запускает главное меню.
 */
public class Books {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            MainMenu mainMenu = context.getBean(MainMenu.class);
            mainMenu.run();
        }
    }
}