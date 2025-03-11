package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления авторами в SQL базе данных.
 * Предоставляет методы для добавления, редактирования, чтения и удаления авторов.
 */
@Repository
@RequiredArgsConstructor
public class AuthorRepository {

    private final SessionFactory sessionFactory;

    /**
     * Добавляет нового автора в базу данных.
     *
     * @param author объект автора для добавления
     * @return добавленный объект автора
     */
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

    /**
     * Возвращает список всех авторов из базы данных.
     *
     * @return список всех авторов
     */
    public List<Author> readAll() {
        List<Author> authors = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
            Root<Author> root = criteriaQuery.from(Author.class);
            criteriaQuery.select(root);
            authors = session.createQuery(criteriaQuery)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .list();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors;
    }

    /**
     * Ищет авторов по части имени автора и возвращает список авторов с книгами.
     *
     * @param name часть имени автора для поиска
     * @return список авторов, соответствующих критерию поиска
     */
    public List<Author> findByName(String name) {
        List<Author> authors = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = cb.createQuery(Author.class);
            Root<Author> rootAuthor = criteriaQuery.from(Author.class);
            rootAuthor.fetch("books", JoinType.LEFT);

            criteriaQuery.select(rootAuthor).distinct(true)
                    .where(cb.like(rootAuthor.get("name"), "%" + name + "%"));
            authors = session.createQuery(criteriaQuery)
                    .list();

            session.getTransaction().commit();
            return authors;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors;
    }

    /**
     * Проверяет, существует ли автор с заданным именем.
     *
     * @param authorName имя автора для проверки
     * @return true, если автор с таким именем существует, иначе false
     */
    public boolean existByName(String authorName) {
        Long count = countByName(authorName);
        return count != null && count > 0;
    }

    /**
     * Проверяет, существует ли автор с заданным идентификатором.
     *
     * @param id идентификатор автора для проверки
     * @return true, если автор с таким идентификатором существует, иначе false
     */
    public boolean existById(int id) {
        return findById(id) != null;
    }

    /**
     * Обновляет информацию об авторе в базе данных.
     *
     * @param author объект автора с обновленными данными
     */
    public void edit(Author author) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(author);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет автора с заданным идентификатором из базы данных.
     *
     * @param id идентификатор автора для удаления
     */
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Author author = session.get(Author.class, id);
            if (author != null) {
                session.delete(author);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет всех авторов из базы данных.
     */
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Author").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Подсчитывает количество авторов с заданным именем.
     *
     * @param authorName имя автора для подсчета
     * @return количество авторов с таким именем
     */
    private Long countByName(String authorName) {
        Long count = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Author> rootAuthor = criteriaQuery.from(Author.class);
            criteriaQuery.select(criteriaBuilder.count(rootAuthor))
                    .where(criteriaBuilder.equal(rootAuthor.get("name"), authorName));
            count = session.createQuery(criteriaQuery)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Ищет автора по идентификатору.
     *
     * @param id идентификатор автора для поиска
     * @return найденный объект автора или null, если автор не найден
     */
    private Author findById(int id) {
        Author author = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = cb.createQuery(Author.class);
            Root<Author> rootAuthor = criteriaQuery.from(Author.class);

            criteriaQuery.select(rootAuthor)
                    .where(cb.equal(rootAuthor.get("id"), id));

            author = session.createQuery(criteriaQuery)
                    .setHint("org.hibernate.cacheable", true)
                    .setHint("org.hibernate.readOnly", true)
                    .uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }
}