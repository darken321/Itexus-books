package org.example.repository;


import org.example.model.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления книгами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    default List<Book> findAllOrderByIdAsc() {
        return findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorNameContainingIgnoreCase(String authorName);

    Optional<Book> findById(Integer id);
}