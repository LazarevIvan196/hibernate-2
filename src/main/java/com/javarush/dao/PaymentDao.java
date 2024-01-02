package com.javarush.dao;

import com.javarush.domain.Payment;
import org.hibernate.SessionFactory;

public class PaymentDao extends GenericDao<Payment>{
    public PaymentDao(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
