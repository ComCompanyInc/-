/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.dto.UserChannelDto;
import com.mycompany.stosjavalin.dto.UserDto;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;
import com.mycompany.stosjavalin.repository.UserChannelRepository;
import com.mycompany.stosjavalin.repository.UserRepository;
import com.mycompany.stosjavalin.service.ConfigData;
import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class UserChannelController {
    private final Javalin javalin;
    
    public UserChannelController(Javalin javalin, SessionFactory sessionFactory) {
        this.javalin = javalin;
        configureRoutes(sessionFactory);
    }
    
    private void configureRoutes(SessionFactory sessionFactory) {
        UserChannelRepository userChannelRepository = new UserChannelRepository(sessionFactory);
        
        javalin.get("/userChannels", ctx -> {
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
            
            User currentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory);
            if (currentUser != null) { //если полученный по токену пользователь не пустой
                
                boolean isActiveUserOnChannel = Boolean.parseBoolean(ctx.queryParam("isActive"));
                
                List<UserChannelDto> userChannelDto = new ArrayList<>();
                for(UserChannel currentUserChannel : userChannelRepository.getUserChannel(currentUser.getId())) {
                    if ((currentUserChannel.getUser().getId() == currentUser.getId()) //если перебираемые каналы принадлежат текущему пользователю (по id)
                        && (currentUserChannel.getIsActiveUserForGroup() == isActiveUserOnChannel)){ //и активность в канале равна параметру
                        userChannelDto.add(new UserChannelDto(currentUserChannel));
                    }
                }
                
                ctx.json(userChannelDto);
            } else {
                ctx.status(403).json("Доступ запрещен! Токен указан некорректно");
            }
        });
        
        javalin.get("/userChannels/usersByChannel", ctx -> {
            Long idChannel = Long.parseLong(ctx.queryParam("idChannel"));
            
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
            
            Long idCurrentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory).getId();
            
            List<User> usersByChannel = userChannelRepository.getUsersByChannel(idChannel, idCurrentUser);
            if (usersByChannel != null) {
                List<UserDto> userDto = new ArrayList<>();
                for (User user : usersByChannel) {
                    userDto.add(new UserDto(user));
                }
                
                ctx.json(userDto);
            } else {
                ctx.json("Данный канал вам недоступен, либо в нем нет ни одного пользователя!");
            }
        });
    }
}
