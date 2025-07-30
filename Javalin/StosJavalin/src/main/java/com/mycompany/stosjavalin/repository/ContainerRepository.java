/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.repository;

import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.Container;
import com.mycompany.stosjavalin.entity.UserChannel;
import com.mycompany.stosjavalin.service.ConfigData;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class ContainerRepository {
    SessionFactory sessionFactory;
    
    public ContainerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public List<Container> findContainersForChannel(Long idChannel, int page, String searchFilter) {
        Session session = sessionFactory.openSession();
        
        String query = "FROM Container c WHERE (c.channel.id = :idChannel)";
        
        List<Container> containers;
        
        if (searchFilter == null) {
            containers = session.createQuery(query)
                .setParameter("idChannel", idChannel)
                .setFirstResult((page - 1) * ConfigData.MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION)
                .setMaxResults(ConfigData.MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION)
                .getResultList();
        } else {
            containers = session.createQuery(query + " AND (c.description ILIKE :filter)")
                .setParameter("idChannel", idChannel)
                .setParameter("filter", "%" + searchFilter + "%")
                .setFirstResult((page - 1) * ConfigData.MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION)
                .setMaxResults(ConfigData.MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION)
                .getResultList();
        }
        
        return containers;
    }
    
    public Container saveContainer(Container container) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(container); // или merge() если нужно обновление
        session.getTransaction().commit();
        session.close();
        return container;
    }
}
