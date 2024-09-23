package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code Book} представляет собой модель книги.
 * Переопределяет метод toString для корректного вывода книги
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

    @Override
    public String toString() {
        String yellow = "\u001B[93m";
        String reset = "\u001B[0m";
        return yellow + "ID " + reset + this.getId() + ", " +
                yellow + "Название "  + reset + this.getTitle() + ", " +
                yellow + "Автор " + reset + this.getAuthor() + ", " +
                yellow + "Описание " + reset + this.getDescription();
    }
}