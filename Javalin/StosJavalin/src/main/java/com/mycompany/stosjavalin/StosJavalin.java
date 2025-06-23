/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.stosjavalin;

import com.mycompany.stosjavalin.controller.ChannelController;
import com.mycompany.stosjavalin.controller.UserChannelController;
import com.mycompany.stosjavalin.controller.UserController;
import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;
import io.javalin.Javalin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author maxim
 */
public class StosJavalin {
    Javalin javalin;
    int port = 7070;
    
    SessionFactory sessionFactory = new Configuration().configure()
            .addAnnotatedClass(User.class) // добавляем классы сущностей под управление HibernateORM
            .addAnnotatedClass(UserChannel.class)
            .addAnnotatedClass(Channel.class)
            .buildSessionFactory();
    
    public static void main(String[] args) {
        StosJavalin app = new StosJavalin();
        app.setJavalinConfig();
        
        app.injection();
    }
    
    public void setJavalinConfig() {
       javalin = Javalin.create(/*config*/); 
    }
    
    public Javalin getJavalinConfig() {
        return javalin;
    }
    
    public void injection() {
        //Важно: сначала регестрируем маршруты, потом добавляем middleware
        UserController userController = new UserController(javalin, sessionFactory);
        UserChannelController userChannelController = new UserChannelController(javalin, sessionFactory);
        ChannelController channelController = new ChannelController(javalin, sessionFactory);
        
        javalin.start(port);
    }
}
