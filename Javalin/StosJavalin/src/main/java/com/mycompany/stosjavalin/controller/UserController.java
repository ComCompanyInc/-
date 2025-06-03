/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.StosJavalin;
import com.mycompany.stosjavalin.repository.UserRepository;
import io.javalin.Javalin;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class UserController {
    private final Javalin javalin;
    
    public UserController(Javalin javalin, int port, SessionFactory sessionFactory) {
        this.javalin = javalin;
        configureRoutes(port, sessionFactory);
    }
    
    private void configureRoutes(int port, SessionFactory sessionFactory) {
        
        
        javalin.get("/users", ctx -> {
            UserRepository userRepository = new UserRepository(sessionFactory);
            ctx.json(userRepository.getAllUsers());
        }).start(port);
    }
}
