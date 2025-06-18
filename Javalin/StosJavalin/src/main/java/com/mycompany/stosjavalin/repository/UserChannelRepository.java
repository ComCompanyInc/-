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
    
    /**
     * Взять пользователей по id канала и id текущего пользователя
     * @param idChannel id канала
     * @param idCurrentUser id текущего пользователя
     * @return лист пользователей в канале
     */
    public List<User> getUsersByChannel(Long idChannel, Long idCurrentUser) {
        Session session = sessionFactory.openSession();
        UserChannel userChannel = session.createQuery("FROM UserChannel uc WHERE (uc.channel.id = :idChannel) AND (uc.user.id = :idCurrentUser)", UserChannel.class)
            .setParameter("idChannel", idChannel)
            .setParameter("idCurrentUser", idCurrentUser)
            .uniqueResult();
        
        if (userChannel != null) {
            return session.createQuery("SELECT uc.user FROM UserChannel uc WHERE (uc.channel.id = :idChannel) AND (uc.isActiveUserForGroup = true)")
                .setParameter("idChannel", idChannel)
                .list();
        } else {
            return null;
        }
    }
}
