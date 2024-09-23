package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервис для управления книгами.
 * Предоставляет методы для создания, редактирования, удаления и вывода списка книг.
 */

@Component
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final String red = "\u001B[31m";
    private final String reset = "\u001B[0m";

    /**
     * Создает новую книгу и добавляет ее в репозиторий.
     *
     * @param book Книга для добавления.
     */
    public void createBook(Book book) {
        if (bookRepository.addBook(book) == null) {
            System.out.println(red + "Ошибка записи в файл, книга не добавлена." + reset);
        } else {
            System.out.println("Книга " + book.getTitle() + " добавлена.");
        }
    }

    /**
     * Выводит список всех книг в консоль.
     */
    public void listBooks() {
        List<Book> books = bookRepository.readBooks();
        if (books.isEmpty()) {
            System.out.println("Список книг пуст.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    /**
     * Редактирует существующую книгу.
     *
     * @param editBook Обновленная книга.
     */
    public void editBook(Book editBook) {
        bookRepository.editBook(editBook);
        System.out.println("Книга " + editBook.getTitle() + " обновлена.");
    }

    /**
     * Удаляет книгу из репозитория по ID.
     *
     * @param id ID книги для удаления.
     */
    public void deleteBook(int id) {
        List<Book> books = bookRepository.readBooks();
        Integer index = findBookById(books, id);
        if (index == null) {
            System.out.println(red + "Книга с таким ID не найдена." + reset);
            return;
        }
        books.remove((int) index);
        bookRepository.deleteBook(books, id);
        System.out.println("Книга удалена.");
    }

    /**
     * Находит индекс книги в списке по ID.
     *
     * @param books Список книг.
     * @param id    ID книги для поиска.
     * @return Индекс книги в списке или null, если книга не найдена.
     */
    private Integer findBookById(List<Book> books, int id) {
        for (int i = 0; i < books.size(); i++) {

            if (books.get(i).getId() == id) {
                return i;
            }
        }
        return null;
    }
}
