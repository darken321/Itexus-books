package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class BookUtils {
    private static final String red = "\u001B[31m";
    private static final String reset = "\u001B[0m";
    private static final String green = "\u001B[92m";
    private static final String yellow = "\u001B[93m";

    /**
     * Выводит список всех книг.
     *
     * @param books список книг для вывода.
     * @param messageSource источник сообщений для интернационализации.
     * @param currentLocale текущая локаль.
     */
    public static void listBooks(List<Book> books, MessageSource messageSource, Locale currentLocale) {
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage("service.listBook", null, currentLocale));
        } else {
            for (Book book : books) {
                System.out.println(
                        yellow + messageSource.getMessage("book.id", null, currentLocale) + ": " + reset + book.getId() + ", " +
                                yellow + messageSource.getMessage("book.title", null, currentLocale) + ": " + reset + book.getTitle() + ", " +
                                yellow + messageSource.getMessage("book.author", null, currentLocale) + ": " + reset + book.getAuthor() + ", " +
                                yellow + messageSource.getMessage("book.description", null, currentLocale) + ": " + reset + book.getDescription()
                );
            }
        }
    }
}

