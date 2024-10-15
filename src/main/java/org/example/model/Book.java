package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code Book} представляет собой модель книги.
 */
@Getter
@Setter
public class Book {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("description")
    private String description;

    @JsonCreator
    public Book(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("author") String author,
            @JsonProperty("description") String description) {
        this.author = author;
        this.description = description;
        this.id = id;
        this.title = title;
    }
}