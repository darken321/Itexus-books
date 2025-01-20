package org.example.repository;


import org.example.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Репозиторий для управления книгами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
public class GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Genre> genreRowMapper = new RowMapper<>() {

        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre(
                    rs.getInt("id"),
                    rs.getString("name")
            );

            return genre;
        }
    };

    public Genre addGenre(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (?) RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class, genre.getName());
        genre.setId(id);
        return genre;
    }

    public Genre findGenre(String genreName) {
        String sql = """
                SELECT *
                FROM genres
                WHERE name = ?
                """;
        return jdbcTemplate.queryForObject(sql, genreRowMapper, genreName);
    }

    public boolean existByGenre(String genre) {
        String sql = "SELECT COUNT(*) FROM genres WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, genre);
        return count != null && count > 0;
    }

    public Integer findGenreId(String genreName) {
        String sql = """
                SELECT id
                FROM genres
                WHERE name = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, genreName);
    }
}