package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.model.Book;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.example.utils.BookUtils;
import org.example.utils.MessageKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

/**
 * Класс {@code MainMenu} отвечает за отображение главного меню и обработку пользовательского ввода.
 */
@Controller
@RequiredArgsConstructor
public class MainMenu {

    private final BookService bookService;
    private final AuthorService authorService;
    private final BookInputHandler bookInputHandler;
    private final MessageSource messageSource;
    private final BookUtils bookUtils;

    @Value("${color.error}")
    private String error;

    @Value("${color.reset}")
    private String reset;

    @Value("${color.text}")
    private String text;


    private Locale currentLocale = Locale.getDefault();

    public void menu() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            language(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Запускает меню выбора языка
     */
    public void language(BufferedReader reader) {

        int input;
        do {
            System.out.println(text);
            System.out.println(messageSource.getMessage(MessageKeys.MENU_LANGUAGE, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_OPTION1, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_OPTION2, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_EXIT, null, currentLocale));
            System.out.println(reset);

            try {
                input = Integer.parseInt(reader.readLine());
                switch (input) {
                    case 1 -> {
                        currentLocale = new Locale("ru");
                        run(reader);
                    }
                    case 2 -> {
                        currentLocale = Locale.ENGLISH;
                        run(reader);
                    }
                    case 0 -> System.out.println(messageSource.getMessage(MessageKeys.MENU_EXIT_MESSAGE, null, currentLocale));
                    default -> System.out.println(error +
                            messageSource.getMessage(MessageKeys.MENU_INVALID, null, currentLocale) +
                            reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(error + messageSource.getMessage(MessageKeys.MENU_NOT_NUMBER, null, currentLocale) + reset);
                input = -1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (input != 0);
    }

    /**
     * Запускает главное меню и обрабатывает пользовательский ввод.
     * Пользователь может выбрать одну из доступных операций или выйти из программы.
     */
    public void run(BufferedReader reader) {

        int input;
        do {
            System.out.println(text);
            System.out.println(messageSource.getMessage(MessageKeys.MENU_ACTION, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_LIST_BOOKS, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_FIND_BOOK, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_CREATE_BOOK, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_EDIT_BOOK, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_DELETE_BOOK, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_DELETE_AUTHOR, null, currentLocale));
            System.out.println(messageSource.getMessage(MessageKeys.MENU_EXIT_ACTION, null, currentLocale));
            System.out.println(reset);

            try {
                input = Integer.parseInt(reader.readLine());
                switch (input) {
                    case 1 -> bookUtils.listBooks(bookService.readAll(currentLocale), messageSource, currentLocale);
                    case 2 -> {
                        String readBookName = bookInputHandler.findBookDetails(currentLocale);
                        List<Book> foundBooks = bookService.findByName(readBookName);
                        bookUtils.listBooks(foundBooks, messageSource, currentLocale);
                    }
                    case 3 -> bookService.add(bookInputHandler.newBookDetails(currentLocale), currentLocale);
                    case 4 -> bookService.edit(bookInputHandler.updateBookDetails(currentLocale), currentLocale);
                    case 5 -> bookService.delete(bookInputHandler.readDeleteDetails(currentLocale), currentLocale);
                    case 6 -> {
                        List<Author> authors = authorService.readAll(currentLocale);
                        bookUtils.listAuthors(authors, messageSource, currentLocale);
                        authorService.delete(bookInputHandler.readDeleteDetails(currentLocale), currentLocale);
                    }
                    case 0 -> System.out.println(messageSource.getMessage(MessageKeys.MENU_EXIT_MESSAGE, null, currentLocale));
                    default -> System.out.println(error + messageSource.getMessage(MessageKeys.MENU_INVALID, null, currentLocale) + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(error + messageSource.getMessage(MessageKeys.MENU_NOT_NUMBER, null, currentLocale) + reset);
                input = -1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (input != 0);
    }
}