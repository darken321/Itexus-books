package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Класс {@code DatabaseInitializerRunner} отвечает за инициализацию базы данных при запуске приложения.
 * Он реализует интерфейс {@link CommandLineRunner}, что позволяет выполнять код после того, как контекст
 * приложения Spring будет полностью инициализирован.
 * При запуске приложения этот класс вызывает методы {@code clearDatabase} и {@code populateDatabase}
 * из класса {@link DatabaseInitializer}, чтобы очистить и заполнить базу данных новыми записями.
 */
@Component
@RequiredArgsConstructor
public class DatabaseInitializerRunner implements CommandLineRunner {

    private final DatabaseInitializer databaseInitializer;

    @Override
    public void run(String... args) throws Exception {
//        databaseInitializer.clearDatabase();
//        databaseInitializer.populateDatabase();
//        databaseInitializer.populateImages();
    }
}