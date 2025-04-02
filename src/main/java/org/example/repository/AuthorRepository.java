package org.example.repository;


import org.example.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для управления авторами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления авторов.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findAuthorsByName(String name);

    Integer countAllByName(String name);
}