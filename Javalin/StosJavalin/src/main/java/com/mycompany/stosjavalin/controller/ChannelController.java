/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.repository.ChannelRepository;
import com.mycompany.stosjavalin.security.JwtSimple;
import com.mycompany.stosjavalin.service.ConfigData;
import io.javalin.Javalin;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class ChannelController {
    private final Javalin javalin;
    
    public ChannelController(Javalin javalin, SessionFactory sessionFactory) {
        this.javalin = javalin;
        configureRoutes(sessionFactory);
    }
    
    private void configureRoutes(SessionFactory sessionFactory) {
        ChannelRepository channelRepository = new ChannelRepository(sessionFactory);
        
        javalin.post("/channel", ctx -> {
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
    
            if (authHeader != null && JwtSimple.checkToken(authHeader.substring(7))) { //убираем первые 7 символов у токена и чекаем токен на валидность
                Channel newChannel = ctx.bodyAsClass(Channel.class); //берет из тесла запроса json и сериализует в обьект данного класса
                
                Channel channel = channelRepository.saveChannel(newChannel);
                
                ctx.status(201).json(channel);
            } else {
                ctx.status(401).json("Вы неавторизированы в системе!");
            }
        });
        
        javalin.put("/channel", ctx -> {
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
    
            User currentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory);
            if (currentUser != null) { //если полученный по токену пользователь не пустой
                // 1. Парсим JSON в объект Channel
                Channel updateUser = ctx.bodyAsClass(Channel.class);

                // 2. Сохраняем обьект в БД
                Channel channel = channelRepository.updateChannel(updateUser);
                ctx.json(channel); // Автоматически в JSON
            } else {
                ctx.status(403).json("Доступ запрещен! Токен указан некорректно");
            }
        });
    }
}
