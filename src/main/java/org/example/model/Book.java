package org.example.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code Book} представляет собой модель книги для БД SQL.
 */
@Getter
@Setter
public class Book {

    private Integer id;
    private String title;
    private String description;
    private Author author;
    private Genre genre;

    public Book(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Book(int id, String title, String description) {
        this(title,description);
        this.id = id;
    }

    public Book(String title, String author, String description, String genre) {
        this(title, description);
        this.author = new Author(author);
        this.genre = new Genre(genre);
    }

    public Book(int id, String title, String author, String description, String genre) {
        this(title, author, description, genre);
        this.id = id;
    }

    public void setAuthorId(Integer authorId) {
        this.setAuthor(new Author(authorId));
    }
    public void setGenreId(Integer genreId) {
        this.setGenre(new Genre(genreId));
    }
}