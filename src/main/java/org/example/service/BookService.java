package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.GenreRepository;
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

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    /**
     * Создает новую книгу и добавляет ее в репозиторий.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param book          Книга для добавления.
     */
    public Book add(Book book, Locale currentLocale) {

        checkGenre(book);
        checkAuthor(book);
        //TODO пофиксить проверку на ошибку и по else вернуть 200
        Book savedBook = bookRepository.save(book);
        if (savedBook == null) {
//            System.out.println(error +
//                    messageSource.getMessage(MessageKeys.SERVICE_FILE_WRITE_ERROR, null, currentLocale) +
//                    reset);
        } else {
//            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_ADD_BOOK, null, currentLocale));
        }
        return savedBook;
    }

    /**
     * Возвращает список книг по части названию книги.
     *
     * @param bookName часть названия книги
     * @return список книг с данным названием без учета заглавных букв.
     */
    public List<Book> findByName(String bookName) {
        return bookRepository.findByTitleContainingIgnoreCase(bookName);
    }


    /**
     * Возвращает список книг по части имени автора.
     *
     * @param authorName часть имени автора
     * @return
     */
    public List<Book> findByAuthorName(String authorName) {
        return bookRepository.findByAuthorNameContainingIgnoreCase(authorName);
    }

    /**
     * Возвращает список всех книг, отсортированный по ID
     */
    public List<Book> readAll(Locale currentLocale) {
        return bookRepository.findAllOrderByIdAsc();
    }

    /**
     * Редактирует существующую книгу.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param book          Книга, которую нужно обновить.
     */
    public Book edit(Book book, Locale currentLocale) {
        Book updated = null;
        if (book != null) {
            checkAuthor(book);
            checkGenre(book);
            updated = bookRepository.save(book);
//            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_EDIT_BOOK, null, currentLocale));
        }
        return updated;
    }

    /**
     * Удаляет книгу из репозитория по ID.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id            ID книги для удаления.
     */
    public void delete(int id, Locale currentLocale) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
//            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_DELETE_BOOK, null, currentLocale));
        } else {
//            System.out.println(error + messageSource.getMessage(MessageKeys.NOT_FOUND_BY_ID,
//                    null, currentLocale) + reset);
        }
    }

    /**
     * Проверяет наличие автора в базе данных. Если автор не существует, добавляет нового автора.
     * Если автор существует, обновляет объект книги с данными существующего автора.
     *
     * @param book Книга, для которой необходимо проверить и установить автора.
     */
    private void checkAuthor(Book book) {
        //автора нет
        if (authorRepository.countAllByName(book.getAuthor().getName()) == 0) {
            Author newAuthor = authorService.save(book.getAuthor());
            book.setAuthor(newAuthor);
        } else {
            Author oldAuthor = authorService.findByName(book.getAuthor().getName()).get(0);
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
        if (genreRepository.countAllByName(book.getGenre().getName()) == 0) {
            Genre newGenre = genreRepository.save(book.getGenre());
            book.setGenre(newGenre);
        } else {
            Genre oldGenre = genreRepository.findByName(book.getGenre().getName());
            book.setGenre(oldGenre);
        }
    }
}