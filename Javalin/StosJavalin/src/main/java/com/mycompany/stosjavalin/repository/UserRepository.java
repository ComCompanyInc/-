/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.repository;

import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.service.ConfigData;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 *
 * @author maxim
 */
public class UserRepository {
    SessionFactory sessionFactory;
    
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    private boolean validateFilters(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    public List<User> getUsersByParam(String code, String firstName, String lastName, Integer pageNumber) {
        Session session = sessionFactory.openSession();
        
        String query = "FROM User u";
        
        boolean hasCondition = false;
        
        if (hasCondition && this.validateFilters(code)) {
            query += " OR u.code = :code";
            
        } else if (this.validateFilters(code)) {
            query += " WHERE u.code = :code";
            hasCondition = true;
        }
        if (hasCondition  && this.validateFilters(firstName)) {
            query += " OR u.firstName = :firstName";
        } else if (this.validateFilters(firstName)) {
            query += " WHERE u.firstName = :firstName";
            hasCondition = true;
        }
        if (hasCondition && this.validateFilters(lastName)) {
            query += " OR u.lastName = :lastName";
        } else if (this.validateFilters(lastName)) {
            query += " WHERE u.lastName = :lastName";
            hasCondition = true;
        }
        
        Query<User> users = session.createQuery(query, User.class);
        
        if (this.validateFilters(code)) {
            users.setParameter("code", code);
        }
        if (this.validateFilters(firstName)) {
            users.setParameter("firstName", firstName);
        }
        if (this.validateFilters(lastName)) {
            users.setParameter("lastName", lastName);
        }
        
        return users
                .setMaxResults(pageNumber * ConfigData.MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION)
                .setFirstResult((pageNumber - 1) * ConfigData.MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION)
                .getResultList();
    }
    
    public User getUserByLogin(String login) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM User WHERE login = :login", User.class)
                          .setParameter("login", login)
                          .uniqueResult();
        } finally {
            session.close();
        }
    }
    
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("FROM User", User.class).list();
        return users;
    }
    
    public User getUserById(Long id) {
        Session session = sessionFactory.openSession();
        User user = session.createQuery("FROM User WHERE id = :id", User.class)
                     .setParameter("id", id)
                     .uniqueResult();
        session.close();
        return user;
    }
    
    public User saveUser(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user); // или merge() если нужно обновление
        session.getTransaction().commit();
        session.close();
        return user;
    }
    
    public User updateUser(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.merge(user); // или merge() если нужно обновление
        session.getTransaction().commit();
        session.close();
        return user;
    }
    
    public User deleteUser(Long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = this.getUserById(id);
        session.remove(user); // или merge() если нужно обновление
        session.getTransaction().commit();
        session.close();
        return user;
    }
}
