package org.example.repository;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.model.Book;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Репозиторий для управления книгами в CSV файле.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
public class BookRepository {
    private final String filePath = "src/main/resources/books.csv";

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
     * Записывает в файл список книг с измененной книгой
     *
     * @param updatedBooks Список книг с обновленной книгой.
     */
    public void editBook(List<Book> updatedBooks) {
        writeBooks(updatedBooks);
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
     * Записывает в файл список книг без удаленной книги
     *
     * @param books Список книг с уже удаленной книгой.
     * @param id    ID книги для удаления.
     */
    public void deleteBook(List<Book> books, int id) {
        writeBooks(books);
    }
}
