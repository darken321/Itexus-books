package org.example.mapper;

import org.example.DTO.BookDto;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;

import java.util.List;

/**
 * Класс {@code BookMapper} предоставляет методы для преобразования между
 * объектами модели {@link Book} и объектами передачи данных {@link BookDto}.
 * Этот класс используется для упрощения передачи данных между слоями приложения.
 */
public class BookMapper {

    /**
     * Преобразует объект модели {@link Book} в объект передачи данных {@link BookDto}.
     *
     * @param book объект модели {@link Book}, который нужно преобразовать
     * @return объект {@link BookDto}, представляющий данные книги
     */
    public static BookDto toDTO(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setAuthorName(book.getAuthor().getName());
        dto.setGenreName(book.getGenre().getName());
        return dto;
    }

    public static List<BookDto> AllToBookDto(List<Book> books) {
        return books.stream().map(BookMapper::toDTO).toList();
    }

    /**
     * Преобразует объект передачи данных {@link BookDto} в объект модели {@link Book}.
     *
     * @param dto объект {@link BookDto}, который нужно преобразовать
     * @return объект {@link Book}, представляющий данные книги
     */
    public static Book fromDTO(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setAuthor(new Author(dto.getAuthorName()));
        book.setGenre(new Genre(dto.getGenreName()));
        return book;
    }
}