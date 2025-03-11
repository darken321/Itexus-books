package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.utils.MessageKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Класс для обработки ввода данных от пользователя.
 * Этот класс отвечает за получение данных о книгах от пользователя через консоль.
 */
@Controller
@RequiredArgsConstructor
public class BookInputHandler {

    @Value("${color.error}")
    private String error;

    @Value("${color.reset}")
    private String reset;

    private final MessageSource messageSource;
    private final BookRepository bookRepository;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    /**
     * Получает данные о новой книге от пользователя.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return книгу {@link Book}, для добавления в файл
     */
    public Book newBookDetails(Locale currentLocale) {

        System.out.println(messageSource.getMessage(MessageKeys.READ_NEW_TITLE, null, currentLocale));
        String title = readLine();

        System.out.println(messageSource.getMessage(MessageKeys.READ_NEW_AUTHOR, null, currentLocale));
        String author = readLine();

        System.out.println(messageSource.getMessage(MessageKeys.READ_NEW_DESCRIPTION, null, currentLocale));
        String description = readLine();

        System.out.println(messageSource.getMessage(MessageKeys.READ_NEW_GENRE, null, currentLocale));
        String genre = readLine();

        return new Book(title, author, description, genre);
    }

    /**
     * Выводит сообщение message и
     * запрашивает у пользователя данные
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return строку с названием книги или именем автора.
     */
    public String readDetails(Locale currentLocale, String message) {
        System.out.println(messageSource.getMessage(message, null, currentLocale));
        return readLine();
    }

    /**
     * Запрашивает у пользователя данные для редактирования существующей книги.
     * Если книги с таким id нет, то выводит ошибку и возвращает null
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return книгу {@link Book} с данными для изменения книги.
     */
    public Book updateBookDetails(Locale currentLocale) {

        int id = getValidId(
                messageSource.getMessage(MessageKeys.READ_NEW_ID, null, currentLocale), currentLocale);

        if (bookRepository.existById(id)) {
            System.out.println(messageSource.getMessage(MessageKeys.READ_ADD_TITLE, null, currentLocale));
            String title = readLine();

            System.out.println(messageSource.getMessage(MessageKeys.READ_ADD_AUTHOR, null, currentLocale));
            String author = readLine();

            System.out.println(messageSource.getMessage(MessageKeys.READ_ADD_DESCRIPTION, null, currentLocale));
            String description = readLine();

            System.out.println(messageSource.getMessage(MessageKeys.READ_ADD_GENRE, null, currentLocale));
            String genre = readLine();

            return new Book(id, title, author, description, genre);
        } else {
            System.out.println(error + messageSource.getMessage(MessageKeys.NOT_FOUND_BY_ID,
                    null, currentLocale) + reset);
            return null;
        }
    }

    /**
     * Запрашивает у пользователя валидный ID для удаления.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @return ID, введенный пользователем.
     */
    public int readDeleteDetails(Locale currentLocale) {
        return getValidId(
                messageSource.getMessage(MessageKeys.READ_DELETE_ID, null, currentLocale), currentLocale);
    }

    /**
     * Запрашивает у пользователя корректный ID,
     * проверяет, что введенное значение является положительным числом.
     *
     * @param prompt        сообщение, отображаемое пользователю для ввода ID.
     * @param currentLocale локаль языка, установленная пользователем.
     * @return корректный ID.
     */
    private int getValidId(String prompt, Locale currentLocale) {
        int id = -1;
        while (id < 0) {
            System.out.println(prompt);
            try {
                id = Integer.parseInt(readLine());
                if (id < 0) {
                    System.out.println(
                            error +
                                    messageSource.getMessage(MessageKeys.INVALID_ID, null, currentLocale) +
                                    reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        error +
                                messageSource.getMessage(MessageKeys.NOT_NUMBER, null, currentLocale) +
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
            return reader.readLine().trim();
        } catch (IOException e) {
            return "";
        }
    }
}