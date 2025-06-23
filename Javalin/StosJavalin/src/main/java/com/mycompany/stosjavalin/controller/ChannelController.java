/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.dto.UserChannelDto;
import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;
import com.mycompany.stosjavalin.repository.ChannelRepository;
import com.mycompany.stosjavalin.repository.UserChannelRepository;
import com.mycompany.stosjavalin.security.JwtSimple;
import com.mycompany.stosjavalin.service.ConfigData;
import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.List;
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
        
        /**
         * {
                "name": "blablalba ...",
                "description": "qweqweqwe1",
                "creationDate": "11-11-2012",
                "userChannel": []
            }
         */
        javalin.put("/channel", ctx -> {
            UserChannelRepository userChannelRepository = new UserChannelRepository(sessionFactory);
    
            Long idChannel = Long.parseLong(ctx.queryParam("idChannel"));
            
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
            
            User currentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory);
            
            List<User> usersByChannel = userChannelRepository.getUsersByChannel(idChannel, currentUser.getId());
            
            if (usersByChannel != null) {
                for(UserChannel currentUserChannel : userChannelRepository.getUserChannel(currentUser.getId())) {
                    
                    System.out.println("IF (" + currentUserChannel.getUser().getId() + " =? " + currentUser.getId() + " &&");
                    System.out.println(currentUserChannel.getIsAuthorUserForGroup() + " =? true)");
                    System.out.println("______");
                    System.out.println(currentUserChannel.getChannel().getName());
                    System.out.println(currentUserChannel.getIsAuthorUserForGroup());
                    
                    if ((currentUserChannel.getUser().getId() == currentUser.getId()) //если перебираемые каналы принадлежат текущему пользователю (по id)
                        && (currentUserChannel.getIsAuthorUserForGroup() == true)){ //и пользователь является администратором канала канала
                        
                        System.out.println("DUMP122 = ");

                        // 1. Парсим JSON в объект Channel
                        Channel updateChannel = ctx.bodyAsClass(Channel.class);
                        
                        System.out.println(updateChannel.getId());
                        System.out.println(updateChannel.getName());
                        System.out.println(updateChannel.getDescription());
                        System.out.println(updateChannel.getCreationDate());
                        
                        //меняем нашему измененному обьекту свойства которые мог бы задать клиент (для защиты)
                        updateChannel.setId(idChannel); // задаем текущий id чтобы не было возможности изменить левую запись с другим id
                        //?? updateChannel.setUserChannel(new ArrayList<UserChannel>()); // задаем пустой массив чтобы не было возможности создать userChannel из channel
                        
                        // 2. Сохраняем обьект в БД
                        Channel channel = channelRepository.updateChannel(updateChannel);
                        
                        System.out.println("ответ json: \n");
                        System.out.println(channel.getId());
                        System.out.println(channel.getName());
                        System.out.println(channel.getDescription());
                        System.out.println(channel.getCreationDate());
                        
                        ctx.json(channel); // Автоматически в JSON
                    } else {
                        ctx.json("Ошибка - вы не можете обновить запись потому что не являетесь администратором канала!");
                    }
                }

            } else {
                ctx.json("Данный канал вам недоступен, либо в нем нет ни одного пользователя!");
            }
        });
    }
}
