package org.example.controller;

import lombok.RequiredArgsConstructor;

import org.example.DTO.BookDto;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Контроллер для управления книгами.
 * Предоставляет API для создания, обновления, удаления и получения книг.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final Locale currentLocale = Locale.getDefault();

    /**
     * Получает книгу по её идентификатору.
     *
     * @param id идентификатор книги
     * @return книга в формате DTO или ответ с кодом 404 (Not Found), если книга не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable int id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            BookDto bookDTO = BookMapper.toDTO(optionalBook.get());
            return ResponseEntity.ok(bookDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получает список книг по названию.
     * Если название не указано, возвращает все книги.
     *
     * @param title название книги (необязательный параметр)
     * @return список книг в формате DTO
     */
    @GetMapping
    public ResponseEntity<List<BookDto>> getBookByName(@RequestParam(required = false) String title) {
        List<Book> books;
        if (title == null) {
            books = bookService.readAll(currentLocale);
        } else {
            books = bookService.findByName(title);
        }
        List<BookDto> bookDTOs = BookMapper.AllToBookDto(books);
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Создает новую книгу.
     *
     * @param bookDTO данные книги в формате DTO
     * @return созданная книга в формате DTO с кодом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDTO) {
        Book book = BookMapper.fromDTO(bookDTO);
        Book createdBook = bookService.add(book, currentLocale);
        BookDto returnDTO = BookMapper.toDTO(createdBook);
        return ResponseEntity.status(201).body(returnDTO);
    }

    /**
     * Обновляет существующую книгу.
     *
     * @param id идентификатор книги
     * @param bookDTO данные книги в формате DTO
     * @return обновленная книга в формате DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable int id, @RequestBody BookDto bookDTO) {
        Book book = BookMapper.fromDTO(bookDTO);
        book.setId(id);
        Book updatedBook = bookService.edit(book, currentLocale);
        BookDto updatedDto = BookMapper.toDTO(updatedBook);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Удаляет книгу по её идентификатору.
     *
     * @param id идентификатор книги
     * @return пустой ответ с кодом 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        bookService.delete(id, currentLocale);
        return ResponseEntity.noContent().build();
    }
}