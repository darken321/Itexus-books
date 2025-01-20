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
    public void createBook(Book book, Locale currentLocale) {

        checkGenre(book);
        checkAuthor(book);

        //Пробую записать книгу через репозиторий или вывести ошибку записи
        if (bookRepository.addBook(book) == null) {
            System.out.println(error +
                    messageSource.getMessage(MessageKeys.SERVICE_FILE_WRITE_ERROR, null, currentLocale) +
                    reset);
        } else {
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_ADD_BOOK, null, currentLocale));
        }
    }

    /**
     * Возвращает список всех книг или null если список пуст.
     */
    public List<Book> readBooks(Locale currentLocale) {
        List<Book> books = bookRepository.readBooks();
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
    public void editBook(Book updatedBook, Locale currentLocale) {
        if (updatedBook != null) {
            checkAuthor(updatedBook);
            checkGenre(updatedBook);
            bookRepository.editBook(updatedBook);
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_EDIT_BOOK, null, currentLocale));
        }
    }

    /**
     * Удаляет книгу из репозитория по ID.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id            ID книги для удаления.
     */
    public void deleteBook(int id, Locale currentLocale) {
        if (bookRepository.existById(id)) {
            bookRepository.deleteBook(id);
            System.out.println(messageSource.getMessage(MessageKeys.SERVICE_DELETE_BOOK, null, currentLocale));
        } else {
            System.out.println(error + messageSource.getMessage(MessageKeys.NOT_FOUND_BOOK_BY_ID,
                    null, currentLocale) + reset);
        }
    }

    /**
     * Возвращает список книг по названию книги.
     *
     * @param bookName Название книги
     * @return список книг с данным названием без учета заглавных букв.
     */
    public List<Book> findBooksByName(String bookName) {
        return bookRepository.findBooksByName(bookName);
    }

    /**
     * Проверяет наличие автора в базе данных. Если автор не существует, добавляет нового автора.
     * Если автор существует, обновляет объект книги с данными существующего автора.
     *
     * @param book Книга, для которой необходимо проверить и установить автора.
     */
    private void checkAuthor(Book book) {
        if (!authorRepository.existByAuthor(book.getAuthor().getName())) {
            Author newAuthor = authorRepository.addAuthor(book.getAuthor());
            book.setAuthor(newAuthor);
        } else {
            Author oldAuthor = authorRepository.findAuthor(book.getAuthor().getName());
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
        if (!genreRepository.existByGenre(book.getGenre().getName())) {
            Genre newGenre = genreRepository.addGenre(book.getGenre());
            book.setGenre(newGenre);
        } else {
            Genre oldGenre = genreRepository.findGenre(book.getGenre().getName());
            book.setGenre(oldGenre);
        }
    }
}
