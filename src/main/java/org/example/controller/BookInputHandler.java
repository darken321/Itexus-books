package org.example.controller;

import org.example.model.Book;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Класс для обработки ввода данных о книгах от пользователя.
 * Этот класс отвечает за получение данных о книгах от пользователя через консоль.
 */
@Component
public class BookInputHandler {

    private final String red = "\u001B[31m";
    private final String reset = "\u001B[0m";

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    /**
     * Получает данные о новой книге от пользователя.
     *
     * @return книгу {@link Book}, для добавления в файл
     */
    public Book newBookDetails() {
        int id = getValidBookId("Введите ID добавляемой книги:");

        System.out.println("Введите название книги:");
        String title = readLine();

        System.out.println("Введите автора книги:");
        String author = readLine();

        System.out.println("Введите описание книги:");
        String description = readLine();

        return new Book(id, title, author, description);
    }

    /**
     * Запрашивает у пользователя данные для редактирования существующей книги.
     *
     * @return книгу {@link Book} с данными для изменения книги.
     */
    public Book updateBookDetails() {

        int id = getValidBookId("Введите ID книги для редактирования:");

        System.out.println("Введите новое название книги");
        String title = readLine();

        System.out.println("Введите нового автора книги");
        String author = readLine();

        System.out.println("Введите новое описание книги");
        String description = readLine();

        return new Book(id, title, author, description);
    }

    /**
     * Запрашивает у пользователя ID книги для удаления.
     *
     * @return ID книги, введенный пользователем.
     */
    public int deleteBookDetails() {
        return getValidBookId("Введите ID книги для удаления:");
    }

    /**
     * Запрашивает у пользователя корректный ID книги,
     * проверяет, что введенное значение является положительным числом.
     *
     * @param prompt сообщение, отображаемое пользователю для ввода ID.
     * @return корректный ID книги.
     */
    private int getValidBookId(String prompt) {
        int id = -1;
        while (id < 0) {
            System.out.println(prompt);
            try {
                id = Integer.parseInt(readLine());
                if (id < 0) {
                    System.out.println(red + "ID книги не может быть отрицательным. Попробуйте снова." + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Неверный ввод. Пожалуйста, введите числовое значение для ID." + reset);
            }
        }
        return id;
    }

    /**
     * Читает строку из консоли.
     *
     * @return введенная строка.
     */
    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println(red + "Ошибка ввода. Попробуйте снова." + reset);
            return "";
        }
    }
}

