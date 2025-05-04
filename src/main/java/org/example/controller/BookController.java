package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.BookDto;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
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

    /**
     * Получает книгу по её идентификатору.
     *
     * @param id идентификатор книги
     * @return книга в формате DTO или ответ с кодом 404 (Not Found), если книга не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable int id) {
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()) {
            BookDto bookDTO = BookMapper.toDto(optionalBook.get());
            return ResponseEntity.ok(bookDTO);
        } else {
            throw new NoSuchElementException("Book with ID " + id + " not found");
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
            books = bookService.readAll();
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
        Book createdBook = bookService.add(book);
        BookDto returnDTO = BookMapper.toDto(createdBook);
        return ResponseEntity.status(201).body(returnDTO);
    }

    /**
     * Обновляет существующую книгу.
     *
     * @param id      идентификатор книги
     * @param bookDTO данные книги в формате DTO
     * @return обновленная книга в формате DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable int id, @RequestBody BookDto bookDTO) {
        Book book = BookMapper.fromDTO(bookDTO);
        book.setId(id);
        Book updatedBook = bookService.edit(book);
        if (updatedBook == null) {
            throw new NoSuchElementException("Book with ID " + id + " not found");
        }
        BookDto updatedDto = BookMapper.toDto(updatedBook);
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
        if (!bookService.existsById(id)) {
            throw new NoSuchElementException("Book with ID " + id + " not found");
        }
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Метод загружает файл изображения для книги по её ID.
     *
     * @param bookId ID книги, к которой нужно загрузить изображение.
     * @param file   файл изображения, который необходимо загрузить.
     * @return ResponseEntity с обновленной книгой.
     */
    @PostMapping(value = "/{bookId}/image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(
            @PathVariable int bookId,
            @RequestParam("file") MultipartFile file) {

        try {
            bookService.addBookImage(bookId, file);
            return ResponseEntity.ok("File " + file.getOriginalFilename() + " added to book with id " + bookId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Метод для скачивания изображения книги из MongoDB по ID книги.
     *
     * @param bookId ID книги, изображение которой нужно скачать.
     * @return ResponseEntity с изображением в виде Resource и заголовком Content-Disposition.
     */
    @GetMapping("/{bookId}/download-image")
    public ResponseEntity<Resource> downloadBookImage(@PathVariable("bookId") int bookId) {
        try {
            Resource resource = bookService.getBookImageById(bookId);
            String fileName = bookService.getFileNameFromMongo(bookId);

            // Кодирую имя файла для корректного отображения не-ASCII символов
            String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}