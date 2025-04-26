package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


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
    private final ImageService imageService;

    /**
     * Создает новую книгу и добавляет ее в репозиторий.
     *
     * @param book Книга для добавления.
     */
    public Book add(Book book) {
        checkGenre(book);
        checkAuthor(book);
        return bookRepository.save(book);
    }

    /**
     * Возвращает список книг по id книги.
     *
     * @param id идентификатор книги
     * @return список книг с данным названием без учета заглавных букв.
     */
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
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
    public List<Book> readAll() {
        return bookRepository.findAllOrderByIdAsc();
    }

    /**
     * Редактирует существующую книгу.
     *
     * @param book Книга, которую нужно обновить.
     */
    public Book edit(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            throw new NoSuchElementException("Book with ID " + book.getId() + " not found");
        }
        checkAuthor(book);
        checkGenre(book);
        return bookRepository.save(book);
    }

    /**
     * Удаляет книгу из репозитория по ID.
     *
     * @param id ID книги для удаления.
     */
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    /**
     * Проверяет наличие книги по id в базе данных
     *
     * @param id идентификатор книги
     */
    public boolean existsById(int id) {
        return bookRepository.existsById(id);
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

    /**
     * Добавляет новую книгу с изображением в репозиторий.
     *
     * Этот метод загружает изображение, путь к которому указан в imagePath, в базу данных MongoDB с использованием GridFS.
     * После загрузки изображения, его уникальный идентификатор сохраняется в объекте книги.
     * Затем книга с этим идентификатором сохраняется в репозитории PostgreSQL.
     *
     * @param book объект книги, который необходимо добавить.
     * @param imagePath путь к файлу изображения, который необходимо загрузить.
     * @throws IOException если возникает ошибка при загрузке изображения.
     */
    public void addBookWithImage(Book book, String imagePath) throws IOException {
        String imageFileId = imageService.uploadImage(imagePath);
        book.setImageFileId(imageFileId);
        bookRepository.save(book);
    }
}