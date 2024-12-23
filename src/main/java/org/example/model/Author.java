package org.example.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code Authors} представляет собой модель автора для БД SQL.
 */
@Getter
@Setter
public class Author {

    private Integer id;

    private String name;

    public Author(Integer id) {
        this.id = id;
    }

    public Author(String name) {
        this.name = name;
    }

    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }
}