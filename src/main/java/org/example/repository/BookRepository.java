package org.example.repository;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.model.Book;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Репозиторий для управления книгами в CSV файле.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Component
public class BookRepository {
    private final String filePath =  "src/main/resources/books.csv";

    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = CsvSchema.builder()
            .addColumn("id")
            .addColumn("title")
            .addColumn("author")
            .addColumn("description")
            .setUseHeader(true)
            .setQuoteChar('"')
            .build();

    /**
     * Добавляет книгу в CSV файл.
     *
     * @param book Книга для добавления.
     * @return Добавленная книга.
     * @throws RuntimeException если произошла ошибка при записи в CSV файл.
     */
    public Book addBook(Book book) {
        File csvFile = new File(filePath);
        boolean isNewFile = !csvFile.exists();

        try (FileWriter writer = new FileWriter(csvFile, true)) {
            CsvSchema writeSchema = schema.withoutHeader();
            if (isNewFile) {
                writeSchema = schema;
            }
            mapper.writer(writeSchema).writeValue(writer, book);
            return book;
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV file: " + e.getMessage(), e);
        }
    }

    /**
     * Редактирует существующую книгу в CSV файле.
     *
     * @param updatedBook Книга с данными для обновления.
     * @throws RuntimeException если книга с указанным ID не найдена или произошла ошибка при записи в CSV файл.
     */
    public void editBook(Book updatedBook) {
        List<Book> books = readBooks();

        boolean bookFound = false;

        for (Book book : books) {
            if (book.getId() == updatedBook.getId()) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setDescription(updatedBook.getDescription());
                bookFound = true;
                break;
            }
        }

        if (!bookFound) {
            throw new RuntimeException("Book with ID " + updatedBook.getId() + " not found.");
        }

        writeBooks(books);
    }

    /**
     * Записывает список книг в CSV файл.
     *
     * @param books Список книг для записи.
     * @throws RuntimeException если произошла ошибка при записи в CSV файл.
     */
    private void writeBooks(List<Book> books) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8")) {
            mapper.writer(schema).writeValues(writer).writeAll(books);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV file: " + e.getMessage(), e);
        }
    }

    /**
     * Читает список книг из CSV файла.
            *
            * @return Список книг.
            * @throws RuntimeException если произошла ошибка при чтении CSV файла.
            */
    public List<Book> readBooks() {

        File csvInputFile = new File(filePath);
        if (!csvInputFile.exists()) {
            try {
                throw new IOException("CSV file not found: " + csvInputFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Iterator<Book> bookIterator;
        try {
            bookIterator = mapper.readerFor(Book.class)
                    .with(schema)
                    .readValues(csvInputFile);
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }

        List<Book> books = new ArrayList<>();
        while (bookIterator.hasNext()) {
            try {
                books.add(bookIterator.next());
            } catch (RuntimeException e) {
                System.err.println("Error deserializing book: " + e.getMessage());
            }
        }
        return books;
    }

    /**
     * Удаляет книгу из списка и перезаписывает CSV файл.
     *
     * @param books Список книг без удаленной книги.
     * @param id    ID книги для удаления.
     */
    public void deleteBook(List<Book> books, int id) {
        writeBooks(books);
    }
}
