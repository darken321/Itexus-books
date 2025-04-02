package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.DTO.BookDto;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Контроллер для управления авторами.
 * Предоставляет API для получения книг по автору и удаления авторов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/author")
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    /**
     * Получает список книг по имени автора.
     * Если имя автора не указано, возвращает все книги.
     *
     * @param name имя автора (необязательный параметр)
     * @return список книг в формате DTO
     */
    @GetMapping
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@RequestParam(required = false) String name) {
        List<Book> books;
        if (name == null) {
            books = bookService.readAll();
        } else {
            books = bookService.findByAuthorName(name);
        }
        List<BookDto> bookDTOs = books.stream()
                .map(BookMapper::toDto)
                .toList();
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Удаляет автора по его идентификатору.
     *
     * @param id идентификатор автора
     * @return пустой ответ с кодом 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int id) {
        if (!authorService.existsById(id)) {
            throw new NoSuchElementException("Author with ID " + id + " not found");
        }
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
