package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Класс {@code Book} представляет собой модель книги для БД SQL.
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Book(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Book(int id, String title, String description) {
        this(title, description);
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

    public Book(String title, String description, Author author, Genre genre) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.genre = genre;
    }

    public Book(int id, String title, String description, Author author, Genre genre) {
        this(title, description, author, genre);
        this.id = id;
    }

    public void setAuthorId(Integer authorId) {
        this.setAuthor(new Author(authorId));
    }

    public void setGenreId(Integer genreId) {
        this.setGenre(new Genre(genreId));
    }

}