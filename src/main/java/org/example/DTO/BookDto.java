package org.example.DTO;

import lombok.Data;

/**
 * Data Transfer Object (DTO) для представления книги.
 * Этот класс используется для передачи данных о книге между слоями приложения.
 */
@Data
public class BookDto {
    private Integer id;
    private String title;
    private String description;
    private String authorName;
    private String genreName;
}