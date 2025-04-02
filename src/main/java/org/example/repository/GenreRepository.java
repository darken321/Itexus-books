package org.example.repository;


import org.example.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления жанрами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления жанров.
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Genre findByName(String genreName);

    Integer countAllByName(String genreName);
}