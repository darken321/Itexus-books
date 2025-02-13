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

    /**
     * Добавляет новую книгу в базу данных.
     *
     * @param book объект книги для добавления
     * @return добавленный объект книги
     */
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

    /**
     * Возвращает список всех книг из базы данных.
     *
     * @return список всех книг
     */
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

    /**
     * Ищет книги по части названия и возвращает список книг.
     *
     * @param name часть названия книги для поиска
     * @return список книг, соответствующих критерию поиска
     */
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

    /**
     * Проверяет, существует ли книга с заданным идентификатором.
     *
     * @param id идентификатор книги для проверки
     * @return true, если книга с таким идентификатором существует, иначе false
     */
    public boolean existById(int id) {
        return findById(id) != null;
    }

    /**
     * Обновляет информацию о книге в базе данных.
     *
     * @param book объект книги с обновленными данными
     */
    public void edit(Book book) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает книгу с заданным идентификатором.
     *
     * @param id идентификатор книги для проверки
     * @return найденную книгу, если книга с таким идентификатором существует, иначе null
     */
    private Book findById(int id) {
        Book book = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            book = session.createQuery("from Book where id = :id", Book.class)
                    .setParameter("id", id)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    /**
     * Удаляет книгу с заданным идентификатором из базы данных.
     *
     * @param id идентификатор книги для удаления
     */
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

    /**
     * Удаляет все книги из базы данных.
     */
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