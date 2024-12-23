package org.example.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code Genres} представляет собой модель жанра для БД SQL.
 */
@Getter
@Setter
public class Genre {

    private Integer id;

    private String name;

    public Genre(Integer id) {
        this.id = id;
    }

    public Genre(String name) {
        this.name = name;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}