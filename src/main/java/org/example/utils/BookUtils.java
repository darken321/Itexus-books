package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
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
    private String reset;

    @Value("${color.listing}")
    private String listing;

    /**
     * Выводит список всех книг.
     *
     * @param books         список книг для вывода.
     * @param messageSource источник сообщений для интернационализации.
     * @param currentLocale текущая локаль.
     */
    public void listBooks(List<Book> books, MessageSource messageSource, Locale currentLocale) {
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_LIST_EMPTY, null, currentLocale));
        } else {
            for (Book book : books) {
                System.out.println(
                        listing + messageSource.getMessage(MessageKeys.BOOK_ID, null, currentLocale) + ": " +
                                reset + book.getId() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_TITLE, null, currentLocale) + ": " +
                                reset + book.getTitle() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_AUTHOR, null, currentLocale) + ": " +
                                reset + book.getAuthor().getName() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_DESCRIPTION, null, currentLocale) + ": " +
                                reset + book.getDescription() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_GENRE, null, currentLocale) + ": " +
                                reset + book.getGenre().getName()
                );
            }
        }
    }

    /**
     * Выводит список всех авторов с книгами.
     *
     * @param authors       список авторов для вывода.
     * @param messageSource источник сообщений для интернационализации.
     * @param currentLocale текущая локаль.
     */
    public void listAuthors(List<Author> authors, MessageSource messageSource, Locale currentLocale) {
        if (authors.isEmpty()) {
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_LIST_EMPTY, null, currentLocale));
        } else {
            for (Author author : authors) {
                System.out.println(
                        listing + messageSource.getMessage(MessageKeys.BOOK_ID, null, currentLocale) + ": " +
                                reset + author.getId() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_AUTHOR, null, currentLocale) + ": " +
                                reset + author.getName()
                );
            }
        }
    }

    /**
     * Выводит список всех авторов с их книгами.
     *
     * @param authors       список авторов для вывода.
     * @param messageSource источник сообщений для интернационализации.
     * @param currentLocale текущая локаль.
     */
    public void listAuthorsAndBooks(List<Author> authors, MessageSource messageSource, Locale currentLocale) {
        if (authors.isEmpty()) {
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_LIST_EMPTY, null, currentLocale));
        } else {
            for (Author author : authors) {
                System.out.println(
                        listing + messageSource.getMessage(MessageKeys.BOOK_ID, null, currentLocale) + ": " +
                                reset + author.getId() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_AUTHOR, null, currentLocale) + ": " +
                                reset + author.getName()
                );

                if (!author.getBooks().isEmpty()) {
                    for (Book book : author.getBooks()) {
                        System.out.println("  " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_TITLE, null, currentLocale) + ": " +
                                reset + book.getTitle() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_DESCRIPTION, null, currentLocale) + ": " +
                                reset + book.getDescription() + ", " +
                                listing + messageSource.getMessage(MessageKeys.BOOK_GENRE, null, currentLocale) + ": " +
                                reset + book.getGenre().getName()
                        );
                    }
                }
            }
        }
    }
}

