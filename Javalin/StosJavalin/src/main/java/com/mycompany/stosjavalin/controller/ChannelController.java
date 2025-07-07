/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.dto.ChannelDto;
import com.mycompany.stosjavalin.dto.UserChannelDto;
import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;
import com.mycompany.stosjavalin.repository.ChannelRepository;
import com.mycompany.stosjavalin.repository.UserChannelRepository;
import com.mycompany.stosjavalin.security.JwtSimple;
import com.mycompany.stosjavalin.service.ConfigData;
import io.javalin.Javalin;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
            
            if (currentUser != null) {
                List<User> usersByChannel = userChannelRepository.getUsersByChannel(idChannel, currentUser.getId());
            
                if (usersByChannel != null) {
                    Channel channel = null;
                    
                    for(UserChannel currentUserChannel : userChannelRepository.getUserChannel(currentUser.getId())) { 
                        if ((currentUserChannel.getUser().getId() == currentUser.getId()) //если перебираемые каналы принадлежат текущему пользователю (по id)
                            && (currentUserChannel.getChannel().getId() == idChannel) //и перебираемый канал == передаваевому в запросе)
                            && (currentUserChannel.getIsAuthorUserForGroup() == true)){ //и пользователь является администратором канала канала
                            
                            // 1. Парсим JSON в объект Channel
                            Channel updateChannel = ctx.bodyAsClass(Channel.class);

                            //меняем нашему измененному обьекту свойства которые мог бы задать клиент (для защиты)
                            updateChannel.setId(idChannel); // задаем текущий id чтобы не было возможности изменить левую запись с другим id
                            updateChannel.setUserChannel(new ArrayList<UserChannel>()); //?? задаем пустой массив чтобы не было возможности создать userChannel из channel

                            // 2. Сохраняем обьект в БД
                            channel = channelRepository.updateChannel(updateChannel);
                        }
                    }

                    //вывод результата
                    if (channel != null) {
                        ctx.json(channel);
                    } else {
                        ctx.json("Ошибка - данный канал не найден для вашего пользователя, либо вы не являетесь его администратором!");
                    }
                    
                } else {
                    ctx.json("Данный пользователь не найден!");
                }
            } else {
                ctx.json("Данный канал вам недоступен, либо в нем нет ни одного пользователя!");
            }
        });
        
        javalin.patch("/channel/{id}", ctx -> {
            UserChannelRepository userChannelRepository = new UserChannelRepository(sessionFactory);
    
            Long idChannel = Long.parseLong(ctx.pathParam("id"));;
            
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
            
            User currentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory);
            
            if (currentUser != null) {
                List<User> usersByChannel = userChannelRepository.getUsersByChannel(idChannel, currentUser.getId());
            
                if (usersByChannel != null) {
                    ChannelDto channelDto = null;
                    
                    for(UserChannel currentUserChannel : userChannelRepository.getUserChannel(currentUser.getId())) { 
                        if ((currentUserChannel.getUser().getId() == currentUser.getId()) //если перебираемые каналы принадлежат текущему пользователю (по id)
                            && (currentUserChannel.getChannel().getId() == idChannel) //и канал == передаваевому в запросе
                            && (currentUserChannel.getIsAuthorUserForGroup() == true)){ //и пользователь является администратором канала канала

                            // Парсим JSON тело запроса в Map
                            Map<String, Object> updates = ctx.bodyAsClass(Map.class);

                            //берем текущий перебираемый канал пользователя
                            Channel currentChannel = currentUserChannel.getChannel();

                            if (updates.containsKey("name")) {
                                currentChannel.setName((String) updates.get("name"));
                            }
                            if (updates.containsKey("description")) {
                                currentChannel.setDescription((String) updates.get("description"));
                            }
                            if (updates.containsKey("creationDate")) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                                currentChannel.setCreationDate(format.parse((String) updates.get("creationDate")));
                            }

                            //меняем нашему измененному обьекту свойства которые мог бы задать клиент (для защиты)
                            currentChannel.setId(idChannel); // задаем текущий id чтобы не было возможности изменить левую запись с другим id
                            currentChannel.setUserChannel(new ArrayList<UserChannel>()); //?? задаем пустой массив чтобы не было возможности создать userChannel из channel

                            // 2. Сохраняем обьект в БД
                            Channel channel = channelRepository.updateChannel(currentChannel);

                            channelDto = new ChannelDto(channel); // Автоматически в JSON
                        }
                    }
                    
                    //вывод результата
                    if (channelDto != null) {
                        ctx.json(channelDto);
                    } else {
                        ctx.json("");
                    }

                } else {
                    ctx.json("Данный пользователь не найден!");
                }
            } else {
                ctx.json("Данный канал вам недоступен, либо в нем нет ни одного пользователя!");
            }
        });
    }
}
