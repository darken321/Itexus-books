package org.example.repository;


import org.example.model.Author;
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
public class AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Author> authorRowMapper = new RowMapper<>() {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author(
                    rs.getInt("id"),
                    rs.getString("name")
            );

            return author;
        }
    };

    public Author addAuthor(Author author) {
        String sql = "INSERT INTO authors (name) VALUES (?) RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class, author.getName());
        author.setId(id);
        return author;
    }


    public List<Author> readAuthors() {
        String sql = """
                SELECT *
                FROM authors
                """;
        return jdbcTemplate.query(sql, authorRowMapper);
    }

    public Author findAuthor(String authorName) {
        String sql = """
                SELECT *
                FROM authors
                WHERE name = ?
                """;
        return jdbcTemplate.queryForObject(sql, authorRowMapper, authorName);
    }

    public boolean existByAuthor(String  author) {
        String sql = "SELECT COUNT(*) FROM authors WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, author);
        return  count !=null && count>0;
    }

    public Integer findAuthorId(String authorName) {
        String sql = """
                SELECT id
                FROM authors
                WHERE name = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, authorName);
    }
}