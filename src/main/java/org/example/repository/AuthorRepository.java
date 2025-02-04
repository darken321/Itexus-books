package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления Авторами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления Авторов.
 */
@Repository
@RequiredArgsConstructor
public class AuthorRepository {

    private final SessionFactory sessionFactory;

    public Author add(Author author) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(author);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    public Author findByName(String authorName) {
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

    public boolean existByName(String author) {
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

    public boolean existById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long count = session.createQuery("select count(*) from Author where id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Author> readAll() {
        List<Author> authors = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            authors = session.createQuery("from Author", Author.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors;
    }
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Author author = session.get(Author.class, id);
            if (author != null) {
                session.delete(author);
            }
            session.getTransaction().commit();
        }
    }

    public void edit(Author author) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(author);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Author").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}