package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class BookUtils {

    @Value("${color.reset}")
    private  String reset;

    @Value("${color.listing}")
    private String listing;

    /**
     * Выводит список всех книг.
     *
     * @param books список книг для вывода.
     * @param messageSource источник сообщений для интернационализации.
     * @param currentLocale текущая локаль.
     */
    public void listBooks(List<Book> books, MessageSource messageSource, Locale currentLocale) {
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage("service.listBook", null, currentLocale));
        } else {
            for (Book book : books) {
                System.out.println(
                        listing + messageSource.getMessage("book.id", null, currentLocale) + ": " + reset + book.getId() + ", " +
                                listing + messageSource.getMessage("book.title", null, currentLocale) + ": " + reset + book.getTitle() + ", " +
                                listing + messageSource.getMessage("book.author", null, currentLocale) + ": " + reset + book.getAuthor() + ", " +
                                listing + messageSource.getMessage("book.description", null, currentLocale) + ": " + reset + book.getDescription()
                );
            }
        }
    }
}

