package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.BookService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс {@code MainMenu} отвечает за отображение главного меню и обработку пользовательского ввода.
 */
@Component
@RequiredArgsConstructor
public class MainMenu {

    private final BookService bookService;
    private final BookInputHandler bookInputHandler;

    private final String red = "\u001B[31m";
    private final String reset = "\u001B[0m";
    private final String green = "\u001B[92m";

    /**
     * Запускает главное меню и обрабатывает пользовательский ввод.
     * Пользователь может выбрать одну из доступных операций или выйти из программы.
     */
    public void run() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int input;
            do {
                System.out.println(green);
                System.out.println("Выберите действие:");
                System.out.println("1. Вывести список книг");
                System.out.println("2. Создать новую книгу");
                System.out.println("3. Отредактировать книгу");
                System.out.println("4. Удалить книгу");
                System.out.println("0. Выйти");
                System.out.println(reset);

                try {
                    input = Integer.parseInt(reader.readLine());
                    switch (input) {
                        case 1 -> bookService.listBooks();
                        case 2 -> bookService.createBook(bookInputHandler.newBookDetails());
                        case 3 -> bookService.editBook(bookInputHandler.updateBookDetails());
                        case 4 -> bookService.deleteBook(bookInputHandler.deleteBookDetails());
                        case 0 -> System.out.println("Выход...");
                        default -> System.out.println(red + "Неверное число, попробуйте снова." + reset);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(red + "Вы ввели не число, попробуйте снова." + reset);
                    input = -1;
                }
            } while (input != 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
