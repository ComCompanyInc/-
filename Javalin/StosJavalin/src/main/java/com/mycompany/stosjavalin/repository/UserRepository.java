/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.repository;

import com.mycompany.stosjavalin.entity.User;
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
    
    public List<User> getUsersByParam(String code, String firstName, String lastName) {
        Session session = sessionFactory.openSession();
        
        String query = "FROM User u";
        
        Query<User> users = session.createQuery(query, User.class);
        
        if (code != null && !code.isEmpty()) {
            query += " AND u.code = :code";
            users.setParameter("code", code);
        }
        if (firstName != null && !firstName.isEmpty()) {
            query += " AND u.firstName = :firstName";
            users.setParameter("firstName", firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            query += " AND u.lastName = :lastName";
            users.setParameter("lastName", lastName);
        }
        
        return (List<User>) users;
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
