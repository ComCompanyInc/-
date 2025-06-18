/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.repository;

import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class ChannelRepository {
    SessionFactory sessionFactory;
    
    public ChannelRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public Channel saveChannel(Channel channel) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(channel); // или merge() если нужно обновление
        session.getTransaction().commit();
        session.close();
        return channel;
    }
    
    public Channel updateChannel(Channel channel) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.merge(channel); // или merge() если нужно обновление
        session.getTransaction().commit();
        session.close();
        return channel;
    }
}
