package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления книгами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
@RequiredArgsConstructor
public class AuthorRepository {

    private final SessionFactory sessionFactory;

    public Author addAuthor(Author author) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(author);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    public Author findAuthor(String authorName) {
        Author author = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            author = session.createQuery("from Author where name = :authorName", Author.class)
                    .setParameter("authorName", authorName)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    public boolean existByAuthor(String author) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long count = session.createQuery("select count(*) from Author where name = :name", Long.class)
                    .setParameter("name", author)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}