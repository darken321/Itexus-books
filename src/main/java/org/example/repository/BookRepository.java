package org.example.repository;


import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Репозиторий для управления книгами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
@Profile("sql")
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = new RowMapper<>() {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author(
                    rs.getInt("author_id"),
                    rs.getString("author_name")
            );

            Genre genre = new Genre(
                    rs.getInt("genre_id"),
                    rs.getString("genre_name")
            );


            Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    author.getName(),
                    rs.getString("description"),
                    genre.getName()
            );
            return book;
        }
    };

    public Book addBook(Book book) {
        String sql = "INSERT INTO books (title, author_id, description, genre_id) VALUES (?, ?, ?, ?) RETURNING id";
        int id = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                book.getTitle(),
                book.getAuthor().getId(),
                book.getDescription(),
                book.getGenre().getId()
        );
        book.setId(id);
        return book;
    }

    public List<Book> readBooks() {
        String sql = """
                SELECT books.id, books.title, books.description, +
                authors.id AS author_id, authors.name AS author_name, +
                genres.id AS genre_id, genres.name AS genre_name
                FROM books
                JOIN authors ON books.author_id = authors.id
                JOIN genres ON books.genre_id = genres.id
                """;
        List<Book> query = jdbcTemplate.query(sql, bookRowMapper);
        return query;
    }

    public void editBook(Book book) {
        // Обновление данных автора
        String updateAuthorSql = """
        UPDATE authors
        SET name = ?
        WHERE id = ?
    """;
        jdbcTemplate.update(updateAuthorSql, book.getAuthor().getName(), book.getAuthor().getId());

        // Обновление данных жанра
        String updateGenreSql = """
        UPDATE genres
        SET name = ?
        WHERE id = ?
    """;
        jdbcTemplate.update(updateGenreSql, book.getGenre().getName(), book.getGenre().getId());

        // Обновление данных книги
        String updateBookSql = """
        UPDATE books
        SET title = ?, author_id = ?, description = ?, genre_id = ?
        WHERE id = ?
    """;
        jdbcTemplate.update(updateBookSql,
                book.getTitle(),
                book.getAuthor().getId(),
                book.getDescription(),
                book.getGenre().getId(),
                book.getId()
        );
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existById(int id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public List<Book> findBooksByName(String name) {
        String sql = """
                SELECT books.id, books.title, books.description, +
                authors.id AS author_id, authors.name AS author_name, +
                genres.id AS genre_id, genres.name AS genre_name
                FROM books
                JOIN authors ON books.author_id = authors.id
                JOIN genres ON books.genre_id = genres.id
                WHERE title = ?
                """;
        return jdbcTemplate.query(sql, bookRowMapper, name);
    }
}