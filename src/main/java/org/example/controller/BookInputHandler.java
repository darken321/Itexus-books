package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Класс для обработки ввода данных о книгах от пользователя.
 * Этот класс отвечает за получение данных о книгах от пользователя через консоль.
 */
@Controller
@RequiredArgsConstructor
public class BookInputHandler {
    private final String red = "\u001B[31m";
    private final String reset = "\u001B[0m";

    private final MessageSource messageSource;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    /**
     * Получает данные о новой книге от пользователя.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return книгу {@link Book}, для добавления в файл
     */
    public Book newBookDetails(Locale currentLocale) {
        int id = getValidBookId(
                messageSource.getMessage("handler.readAddId", null, currentLocale), currentLocale);

        System.out.println(messageSource.getMessage("handler.readAddTitle", null, currentLocale));
        String title = readLine();

        System.out.println(messageSource.getMessage("handler.readAddAuthor", null, currentLocale));
        String author = readLine();

        System.out.println(messageSource.getMessage("handler.readAddDescription", null, currentLocale));
        String description = readLine();

        return new Book(id, title, author, description);
    }

    /**
     * Запрашивает у пользователя данные для редактирования существующей книги.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return книгу {@link Book} с данными для изменения книги.
     */
    public Book updateBookDetails(Locale currentLocale) {

        int id = getValidBookId(
                messageSource.getMessage("handler.readNewId", null, currentLocale), currentLocale);

        System.out.println(messageSource.getMessage("handler.readNewTitle", null, currentLocale));
        String title = readLine();

        System.out.println(messageSource.getMessage("handler.readNewAuthor", null, currentLocale));
        String author = readLine();

        System.out.println(messageSource.getMessage("handler.readNewDescription", null, currentLocale));
        String description = readLine();

        return new Book(id, title, author, description);
    }

    /**
     * Запрашивает у пользователя ID книги для удаления.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return ID книги, введенный пользователем.
     */
    public int deleteBookDetails(Locale currentLocale) {
        return getValidBookId(
                messageSource.getMessage("handler.readDeleteId", null, currentLocale), currentLocale);
    }

    /**
     * Запрашивает у пользователя корректный ID книги,
     * проверяет, что введенное значение является положительным числом.
     *
     * @param prompt сообщение, отображаемое пользователю для ввода ID.
     * @param currentLocale локаль языка, установленная пользователем.
     * @return корректный ID книги.
     */
    private int getValidBookId(String prompt, Locale currentLocale) {
        int id = -1;
        while (id < 0) {
            System.out.println(prompt);
            try {
                id = Integer.parseInt(readLine());
                if (id < 0) {
                    System.out.println(
                            red +
                            messageSource.getMessage("handler.invalidId", null, currentLocale) +
                            reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        red +
                        messageSource.getMessage("handler.notNumber", null, currentLocale) +
                        reset);
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
            return "";
        }
    }
}

