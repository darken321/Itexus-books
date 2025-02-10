package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления книгами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления книг.
 */
@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final SessionFactory sessionFactory;

    public Book add(Book book) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    public List<Book> readAll() {
        List<Book> books = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            books = session.createQuery("from Book", Book.class)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public void edit(Book book) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.delete(book);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long count = session.createQuery("select count(*) from Book where id = :id", Long.class)
                    .setParameter("id", id)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> findByName(String name) {
        List<Book> books = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            books = session.createQuery("from Book where title like :name", Book.class)
                    .setParameter("name", "%" + name + "%")
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Book").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}