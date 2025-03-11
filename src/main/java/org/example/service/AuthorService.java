package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.example.utils.MessageKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


/**
 * Сервис для управления авторами, хранящимися в БД postgreSQL
 * Предоставляет методы для управления авторами
 */

@Service
@RequiredArgsConstructor
public class AuthorService {

    /**
     * Цвет текста для отображения ошибок.
     */
    @Value("${color.error}")
    private String error;

    /**
     * Цвет текста для сброса цвета.
     */
    @Value("${color.reset}")
    private String reset;

    /**
     * Источник сообщений для локализации.
     */
    private final MessageSource messageSource;

    private final AuthorRepository authorRepository;

    /**
     * Создает нового автора и добавляет ее в репозиторий.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param author        Автор для добавления.
     */
    public void add(Author author, Locale currentLocale) {

        if (authorRepository.add(author) == null) {
            System.out.println(error +
                    messageSource.getMessage(MessageKeys.SERVICE_FILE_WRITE_ERROR, null, currentLocale) +
                    reset);
        } else {
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_ADD_AUTHOR, null, currentLocale));
        }
    }

    /**
     * Возвращает список всех авторов или null если список пуст.
     */
    public List<Author> readAll(Locale currentLocale) {
        List<Author> authors = authorRepository.readAll();
        if (authors.isEmpty()) {
            return null;
        } else {
            return authors;
        }
    }

    /**
     * Возвращает список всех авторов или null если список пуст.
     */
    public List<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    /**
     * Редактирует существующего автора.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param author        Книга, которую нужно обновить.
     */
    public void edit(Author author, Locale currentLocale) {
        if (author != null) {
            authorRepository.edit(author);
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_EDIT_AUTHOR, null, currentLocale));
        }
    }

    /**
     * Удаляет автора из репозитория по ID.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id            ID автора для удаления.
     */
    public void delete(int id, Locale currentLocale) {
        if (authorRepository.existById(id)) {
            try {
                authorRepository.delete(id);
                System.out.println(messageSource.getMessage(MessageKeys.SERVICE_DELETE_AUTHOR, null, currentLocale));
            } catch (Exception e) {
                System.out.println(error + messageSource.getMessage(MessageKeys.DB_ERROR,null, currentLocale) +
                        reset);
            }
        } else {
            System.out.println(error +
                    messageSource.getMessage(MessageKeys.NOT_FOUND_BY_ID, null, currentLocale) +
                    reset);
        }
    }
}
