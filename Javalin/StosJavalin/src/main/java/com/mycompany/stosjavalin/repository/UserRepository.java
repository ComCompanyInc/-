/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.repository;

import com.mycompany.stosjavalin.entity.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class UserRepository {
    SessionFactory sessionFactory;
    
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        return session.createQuery("FROM User", User.class).list();
    }
}
