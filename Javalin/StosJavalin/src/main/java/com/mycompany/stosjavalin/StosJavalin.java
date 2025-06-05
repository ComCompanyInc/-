/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.stosjavalin;

import com.mycompany.stosjavalin.controller.UserController;
import com.mycompany.stosjavalin.entity.User;
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
            .addAnnotatedClass(User.class)
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
        
        javalin.start(port);
    }
}
