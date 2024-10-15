package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.BookService;
import org.example.utils.BookUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Класс {@code MainMenu} отвечает за отображение главного меню и обработку пользовательского ввода.
 */
@Controller
@RequiredArgsConstructor
public class MainMenu {

    private final BookService bookService;
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
            System.out.println(messageSource.getMessage("menu.language", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.option1", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.option2", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.exit", null, currentLocale));
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
                    case 0 -> System.out.println(messageSource.getMessage("menu.exitMessage", null, currentLocale));
                    default -> System.out.println(error +
                                    messageSource.getMessage("menu.invalid", null, currentLocale) +
                                    reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(error + messageSource.getMessage("menu.notNumber", null, currentLocale) + reset);
                input = -1;
            } catch (IOException e) {
                throw new RuntimeException(e); //TODO доработать обработку ошибок
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
            System.out.println(messageSource.getMessage("menu.action", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.listBooks", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.createBook", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.editBook", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.deleteBook", null, currentLocale));
            System.out.println(messageSource.getMessage("menu.exitAction", null, currentLocale));
            System.out.println(reset);

            try {
                input = Integer.parseInt(reader.readLine());
                switch (input) {
                    case 1 -> bookUtils.listBooks(bookService.readBooks(currentLocale), messageSource, currentLocale);
                    case 2 -> bookService.createBook(bookInputHandler.newBookDetails(currentLocale), currentLocale);
                    case 3 -> bookService.editBook(bookInputHandler.updateBookDetails(currentLocale), currentLocale);
                    case 4 -> bookService.deleteBook(bookInputHandler.deleteBookDetails(currentLocale), currentLocale);
                    case 0 -> System.out.println(messageSource.getMessage("menu.exitMessage", null, currentLocale));
                    default ->
                            System.out.println(error + messageSource.getMessage("menu.invalid", null, currentLocale) + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(error + messageSource.getMessage("menu.notNumber", null, currentLocale) + reset);
                input = -1;
            } catch (IOException e) {
                throw new RuntimeException(e); //TODO доработать обработку ошибок
            }
        } while (input != 0);
    }
}