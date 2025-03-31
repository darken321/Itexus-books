package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.repository.AuthorRepository;
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

    private final AuthorRepository authorRepository;

    /**
     * Сохраняет автора в БД
     * @param author автор, которого нужно сохранить
     * @return сохраненный автор
     */
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    /**
     * Возвращает список всех авторов
     */
    public List<Author> readAll(Locale currentLocale) {
        return authorRepository.findAll();
    }

    /**
     * Возвращает список всех авторов по имени
     */
    public List<Author> findByName(String name) {
        return authorRepository.findAuthorsByName(name);
    }

    /**
     * Редактирует существующего автора.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param author        Книга, которую нужно обновить.
     */
    public void edit(Author author, Locale currentLocale) {
        if (author != null) {
            authorRepository.save(author);
//            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_EDIT_AUTHOR, null, currentLocale));
        }
    }

    /**
     * Удаляет автора из репозитория по ID.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id            ID автора для удаления.
     */
    public void delete(int id, Locale currentLocale) {
        if (authorRepository.existsById(id)) {
            try {
                authorRepository.deleteById(id);
//                System.out.println(messageSource.getMessage(MessageKeys.SERVICE_DELETE_AUTHOR, null, currentLocale));
            } catch (Exception e) {
//                System.out.println(error + messageSource.getMessage(MessageKeys.DB_ERROR,null, currentLocale) +
//                        reset);
            }
        } else {
//            System.out.println(error +
//                    messageSource.getMessage(MessageKeys.NOT_FOUND_BY_ID, null, currentLocale) +
//                    reset);
        }
    }
}
