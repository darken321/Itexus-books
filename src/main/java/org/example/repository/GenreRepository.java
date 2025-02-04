package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.model.Genre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления книгами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
@RequiredArgsConstructor
public class GenreRepository {

    private final SessionFactory sessionFactory;

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

    public Genre findByName(String genreName) {
        Genre genre = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            genre = session.createQuery("from Genre where name = :genreName", Genre.class)
                    .setParameter("genreName", genreName)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genre;
    }

    public boolean existByName(String genre) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long count = session.createQuery("select count(*) from Genre where name = :name", Long.class)
                    .setParameter("name", genre)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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