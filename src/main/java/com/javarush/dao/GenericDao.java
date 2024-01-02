package com.javarush.dao;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDao<T> {

    public final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public GenericDao(final Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final int id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    public List<T> getItems(int offset, int count) {
        Query query = getCurrentSession().createQuery("FROM " + clazz.getName(), clazz);
        query.getFirstResult();
        query.getMaxResults();
        return query.getResultList();
    }

    public List<T> findAll() {
        return getCurrentSession().createQuery("FROM " + clazz.getName(), clazz).list();
    }

    public T save(final T entity) {
        getCurrentSession().persist(entity);
        return entity;
    }


    public T update(final T entity) {
        return (T)  getCurrentSession().merge(entity);
    }


public void delete(final T entity) {
 getCurrentSession().remove(entity);
}
    public void deleteByID(final int entityId) {
       final T entity = getById(entityId);
       delete(entity);
    }

protected Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
}
}
