package com.javarush.dao;

import com.javarush.domain.Category;
import org.hibernate.SessionFactory;

public class CategoryDao extends GenericDao<Category> {
    public CategoryDao(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
