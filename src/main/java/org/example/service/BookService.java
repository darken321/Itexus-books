package org.example.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.GenreRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    private final GridFsTemplate gridFsTemplate;

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
     * @return список книг по части имени автора.
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
     * Добавляет изображение к книге с указанным ID.
     * Этот метод загружает изображение в базу данных MongoDB с использованием GridFS,
     * обновляет объект книги с идентификатором загруженного изображения и сохраняет изменения в базе данных PostgreSQL.
     *
     * @param bookId ID книги, к которой нужно добавить изображение.
     * @param file   файл изображения, который необходимо загрузить.
     * @throws IOException            если возникает ошибка при загрузке изображения.
     * @throws NoSuchElementException если книга с указанным ID не найдена.
     */
    public void addBookImage(int bookId, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(5 * 1024 * 1024) // 5MB чанки
                .metadata(new Document("contentType", file.getContentType()));

        // Сохраняем файл и обновляем книгу
        try (InputStream inputStream = file.getInputStream()) {
            // Используем GridFS для сохранения файла в MongoDB
            ObjectId fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), "image/jpeg");
            book.setImageFileId(fileId.toString());
            bookRepository.save(book);
        }
    }

    /**
     * Получает изображение книги по её ID.
     * Этот метод извлекает объект книги из базы данных PostgreSQL, получает идентификатор файла изображения,
     * хранящегося в MongoDB, и возвращает данные изображения в виде массива байтов.
     *
     * @param bookId ID книги, изображение которой нужно получить.
     * @return объект типа {@link Resource}, представляющий данные изображения.
     * @throws IOException если возникает ошибка при чтении данных изображения.
     * @throws NoSuchElementException если книга с указанным ID не найдена.
     */

    public Resource getBookImageById(int bookId) throws IOException {
        GridFSFile file = getFileFromMongo(bookId);
        return gridFsTemplate.getResource(file);
    }

    /**
     * Получает имя файла картинки из mongoDB по ID книги.
     *
     * @param bookId ID книги, имя файла которой нужно получить.
     * @return имя файла.
     */
    public String getFileNameFromMongo(int bookId) {
        GridFSFile file = getFileFromMongo(bookId);
        return file.getFilename();
    }

    /**
     * Получает файл фотографии из mongoDB по id книги
     *
     * @param bookId ID книги, файл фотографии которой нужно получить
     * @return объект типа GridFSFile, содержащий метаданные о файле
     */
    private GridFSFile getFileFromMongo(int bookId) {
        Optional<Book> optionalBook = findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new NoSuchElementException("Book with ID " + bookId + " not found");
        }

        Book book = optionalBook.get();
        String imageFileId = book.getImageFileId();
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(imageFileId))));
    }
}