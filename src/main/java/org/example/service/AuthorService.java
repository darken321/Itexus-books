package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     *
     * @param author автор, которого нужно сохранить
     * @return сохраненный автор
     */
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    /**
     * Возвращает список всех авторов
     */
    public List<Author> readAll() {
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
     * @param author Книга, которую нужно обновить.
     */
    public Author edit(Author author) {
        return authorRepository.save(author);
    }

    /**
     * Удаляет автора из репозитория по ID.
     *
     * @param id ID автора для удаления.
     */
    public void delete(int id) {
        authorRepository.deleteById(id);

    }

    /**
     * Проверяет есть ли автор по ID.
     *
     * @param id ID автора для проверки.
     */
    public boolean existsById(int id) {
        return authorRepository.existsById(id);
    }
}
