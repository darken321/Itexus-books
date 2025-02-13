package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.GenreRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
/**
 * Удаляет все записи в БД и заполняет ее новыми записями
 */
public class DatabaseInitializer {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    private final SessionFactory sessionFactory;

    public void clearDatabase() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
        authorRepository.deleteAll();
    }

    public void populateDatabase() {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Добавление авторов
            Author author1 = new Author("Author 1");
            Author author2 = new Author("Author 2");
            Author author3 = new Author("Author 3");
            Author author4 = new Author("Author 4");
            Author author5 = new Author("Author 5");

            session.save(author1);
            session.save(author2);
            session.save(author3);
            session.save(author4);
            session.save(author5);

            // Добавление жанров
            Genre genre1 = new Genre("Genre 1");
            Genre genre2 = new Genre("Genre 2");
            Genre genre3 = new Genre("Genre 3");
            Genre genre4 = new Genre("Genre 4");
            Genre genre5 = new Genre("Genre 5");

            session.save(genre1);
            session.save(genre2);
            session.save(genre3);
            session.save(genre4);
            session.save(genre5);

            // Добавление книг
            session.save(new Book("Book 1", "Description 1", author1, genre1));
            session.save(new Book("Book 2", "Description 2", author2, genre2));
            session.save(new Book("Book 3", "Description 3", author3, genre3));
            session.save(new Book("Book 4", "Description 4", author4, genre4));
            session.save(new Book("Book 5", "Description 5", author5, genre5));

            // Завершение транзакции
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}