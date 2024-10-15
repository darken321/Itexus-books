package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Сервис для управления книгами.
 * Предоставляет методы для создания, редактирования, удаления и вывода списка книг.
 */

@Service
@RequiredArgsConstructor
public class BookService {

    @Value("${color.error}")
    private String error;

    @Value("${color.reset}")
    private String reset;


    private final BookRepository bookRepository;
    private final MessageSource messageSource;


    /**
     * Создает новую книгу и добавляет ее в репозиторий.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param book          Книга для добавления.
     */
    public void createBook(Book book, Locale currentLocale) {
        if (bookRepository.addBook(book) == null) {
            System.out.println(error +
                    messageSource.getMessage("service.fileWriteError", null, currentLocale) +
                    reset);
        } else {
            System.out.println(messageSource.getMessage("service.addBook", null, currentLocale));
        }
    }

    /**
     * Возвращает список всех книг или null если список пуст.
     */
    public List<Book> readBooks(Locale currentLocale) {
        List<Book> books = bookRepository.readBooks();
        if (books.isEmpty()) {
            return null;
        } else {
            return books;
        }
    }

    /**
     * Редактирует существующую книгу.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param updatedBook Обновленная книга.
     */
    public void editBook(Book updatedBook, Locale currentLocale) {
        List<Book> books = bookRepository.readBooks();
        Integer index = findBookById(books, updatedBook.getId(), currentLocale);
        if (index == null) {
            return;
        }

        for (Book book : books) {
            if (book.getId() == updatedBook.getId()) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setDescription(updatedBook.getDescription());
                break;
            }
        }
        bookRepository.editBook(books);
        System.out.println(messageSource.getMessage("service.editBook", null, currentLocale));
    }

    /**
     * Удаляет книгу из репозитория по ID.
     *
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id ID книги для удаления.
     */
    public void deleteBook(int id, Locale currentLocale) {
        List<Book> books = bookRepository.readBooks();
        Integer index = findBookById(books, id, currentLocale);
        if (index == null) {
            return;
        }
        books.remove((int) index);
        bookRepository.deleteBook(books, id);
        System.out.println(messageSource.getMessage("service.deleteBook", null, currentLocale));
    }

    /**
     * Находит индекс книги в списке по ID.
     *
     * @param books Список книг.
     * @param currentLocale локаль языка, установленная пользователем.
     * @param id    ID книги для поиска.
     * @return Индекс книги в списке или null, если книга не найдена.
     */
    private Integer findBookById(List<Book> books, int id, Locale currentLocale) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                return i;
            }
        }
        System.out.println(error +
                messageSource.getMessage("service.notFoundBookById", null, currentLocale) +
                reset);
        return null;
    }
}
