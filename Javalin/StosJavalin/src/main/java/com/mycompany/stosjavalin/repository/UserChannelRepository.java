/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.repository;

import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class UserChannelRepository {
    SessionFactory sessionFactory;
    
    public UserChannelRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public List<UserChannel> getUserChannel(Long id) {
        Session session = sessionFactory.openSession();
        return session.createQuery("FROM UserChannel", UserChannel.class).list();
    }
}
