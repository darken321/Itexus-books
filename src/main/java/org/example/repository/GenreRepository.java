package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.model.Genre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления жанрами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления жанров.
 */
@Repository
@RequiredArgsConstructor
public class GenreRepository {

    private final SessionFactory sessionFactory;

    /**
     * Добавляет новый жанр в базу данных.
     *
     * @param genre объект жанра для добавления
     * @return добавленный объект жанра
     */
    public Genre add(Genre genre) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(genre);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genre;
    }

    /**
     * Ищет жанр по имени.
     *
     * @param genreName имя жанра для поиска
     * @return найденный объект жанра или null, если жанр не найден
     */
    public Genre findByName(String genreName) {
        Genre genre = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            genre = session.createQuery("from Genre where name = :genreName", Genre.class)
                    .setParameter("genreName", genreName)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genre;
    }


    /**
     * Проверяет, существует ли жанр с заданным именем.
     *
     * @param genre имя жанра для проверки
     * @return true, если жанр с таким именем существует, иначе false
     */
    public boolean existByName(String genre) {
        return findByName(genre) != null;
    }

    /**
     * Удаляет все жанры из базы данных.
     */
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Genre").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}