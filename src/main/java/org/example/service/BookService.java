package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.GenreRepository;
import org.example.utils.MessageKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


/**
 * Сервис для управления книгами, хранящимися в БД postgreSQL
 * Предоставляет методы для создания, редактирования, удаления и вывода списка книг.
 */

@Service
@RequiredArgsConstructor
public class BookService {

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

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    /**
     * Создает новую книгу и добавляет ее в репозиторий.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param book          Книга для добавления.
     */
    public void add(Book book, Locale currentLocale) {

        checkGenre(book);
        checkAuthor(book);

        if (bookRepository.add(book) == null) {
            System.out.println(error +
                    messageSource.getMessage(MessageKeys.SERVICE_FILE_WRITE_ERROR, null, currentLocale) +
                    reset);
        } else {
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_ADD_BOOK, null, currentLocale));
        }
    }

    /**
     * Возвращает список книг по части названию книги.
     *
     * @param bookName часть названия книги
     * @return список книг с данным названием без учета заглавных букв.
     */
    public List<Book> findByName(String bookName) {
        return bookRepository.findByName(bookName);
    }

    /**
     * Возвращает список всех книг или null если список пуст.
     */
    public List<Book> readAll(Locale currentLocale) {
        List<Book> books = bookRepository.readAll();
        if (books.isEmpty()) {
            return null;
        } else {
            return books;
        }
    }

    /**
     * Редактирует существующую книгу.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param updatedBook   Книга, которую нужно обновить.
     */
    public void edit(Book updatedBook, Locale currentLocale) {
        if (updatedBook != null) {
            checkAuthor(updatedBook);
            checkGenre(updatedBook);
            bookRepository.edit(updatedBook);
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_EDIT_BOOK, null, currentLocale));
        }
    }

    /**
     * Удаляет книгу из репозитория по ID.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id            ID книги для удаления.
     */
    public void delete(int id, Locale currentLocale) {
        if (bookRepository.existById(id)) {
            bookRepository.delete(id);
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_DELETE_BOOK, null, currentLocale));
        } else {
            System.out.println(error + messageSource.getMessage(MessageKeys.NOT_FOUND_BY_ID,
                    null, currentLocale) + reset);
        }
    }

    /**
     * Проверяет наличие автора в базе данных. Если автор не существует, добавляет нового автора.
     * Если автор существует, обновляет объект книги с данными существующего автора.
     *
     * @param book Книга, для которой необходимо проверить и установить автора.
     */
    private void checkAuthor(Book book) {
        if (!authorRepository.existByName(book.getAuthor().getName())) {
            Author newAuthor = authorRepository.add(book.getAuthor());
            book.setAuthor(newAuthor);
        } else {
            Author oldAuthor = authorRepository.findByName(book.getAuthor().getName()).get(0);
            book.setAuthor(oldAuthor);
        }
    }

    /**
     * Проверяет наличие жанра в базе данных. Если жанр не существует, добавляет новый жанр.
     * Если жанр существует, обновляет объект книги с данными существующего жанра.
     *
     * @param book Книга, для которой необходимо проверить и установить жанр.
     */
    private void checkGenre(Book book) {
        if (!genreRepository.existByName(book.getGenre().getName())) {
            Genre newGenre = genreRepository.add(book.getGenre());
            book.setGenre(newGenre);
        } else {
            Genre oldGenre = genreRepository.findByName(book.getGenre().getName());
            book.setGenre(oldGenre);
        }
    }
}