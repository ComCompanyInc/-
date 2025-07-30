/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.dto.ChannelDto;
import com.mycompany.stosjavalin.dto.ContainerDto;
import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.Container;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;
import com.mycompany.stosjavalin.repository.ChannelRepository;
import com.mycompany.stosjavalin.repository.ContainerRepository;
import com.mycompany.stosjavalin.repository.UserChannelRepository;
import com.mycompany.stosjavalin.service.ConfigData;
import io.javalin.Javalin;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;

/**
 *
 * @author maxim
 */
public class ContainerController {
    private final Javalin javalin;
    
    public ContainerController(Javalin javalin, SessionFactory sessionFactory) {
        this.javalin = javalin;
        configureRoutes(sessionFactory);
    }
    
    private void configureRoutes(SessionFactory sessionFactory) {
        //Роут со взятием всех контейнеров канала в котором он есть по фильтру канала и фильтру поиска по содержимому сообщения
        javalin.get("channels/{id}/containers", ctx -> {
            UserChannelRepository userChannelRepository = new UserChannelRepository(sessionFactory);
            ContainerRepository containerRepository = new ContainerRepository(sessionFactory);
            
            Long channelId = Long.parseLong(ctx.pathParam("id"));
            
            int paginationPage = ctx.queryParam("page") == null ? 1 : Integer.parseInt(ctx.queryParam("page"));
            
            String searchContainersFilter = ctx.queryParam("searchFilter");
            
            List<Container> containerResults = new ArrayList<Container>();
            
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
            
            User currentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory);
            if (currentUser != null) { //если полученный по токену пользователь не пустой
                
                List<User> usersWithAccess = userChannelRepository.getUsersByChannel(channelId, currentUser.getId());
                
                if (usersWithAccess != null) {
                    for (User user : usersWithAccess) {
                        if (user.getId() == currentUser.getId()) { //если полученные id совпадают, выводим контейнеры выбранного канала
                            containerResults = containerRepository.findContainersForChannel(channelId, paginationPage, searchContainersFilter);
                        }
                    }

                    List<ContainerDto> containersDto = new ArrayList<ContainerDto>();
                    
                    for(UserChannel currentUserChannel : userChannelRepository.getUserChannel(currentUser.getId())) { 
                        if ((currentUserChannel.getUser().getId() == currentUser.getId()) //если перебираемые каналы принадлежат текущему пользователю (по id)
                            && (currentUserChannel.getChannel().getId() == channelId)) //и канал == передаваевому в запросе
                        {
                            
                            for (Container container : containerResults) {
                                if (container.getIsDeleted() == false) { //если контейнер не был помечен как удаленный, то добавляем в результат на получение
                                    containersDto.add(new ContainerDto(container));
                                }
                            }
                            
                        }
                    }
                    
                    ctx.json(containersDto);
                } else {
                    ctx.json("Ошибка: Доступ заблокирован (пользоватебль пустой!)");
                }
                
            } else {
                ctx.json("Ошибка: пользоватебль пустой!");
            }
        });
        
        //сохранение нового контейнера в канале
        /*
        {
            "description": "qwenjjrflkm",
            "dateSending": 1134334800000,
            "isDeleted": false,
            "idNotes": null
        }
        */
        javalin.post("channels/{id}/containers", ctx -> {
            UserChannelRepository userChannelRepository = new UserChannelRepository(sessionFactory);
            ContainerRepository containerRepository = new ContainerRepository(sessionFactory);
            ChannelRepository channelRepository = new ChannelRepository(sessionFactory);
            
            Long idChannel = Long.parseLong(ctx.pathParam("id"));
            
            String authHeader = ctx.header("Authorization"); //берем заголовок из запроса с токеном
            
            User currentUser = ConfigData.translateJwtTockenToUserObject(authHeader, sessionFactory);
            
            if (currentUser != null) {
                List<User> usersByChannel = userChannelRepository.getUsersByChannel(idChannel, currentUser.getId());
            
                if (usersByChannel != null) {
                    ChannelDto channelDto = null;
                    
                    for(UserChannel currentUserChannel : userChannelRepository.getUserChannel(currentUser.getId())) { 
                        if ((currentUserChannel.getUser().getId() == currentUser.getId()) //если перебираемые каналы принадлежат текущему пользователю (по id)
                            && (currentUserChannel.getChannel().getId() == idChannel) //и канал == передаваевому в запросе
                        ){
                            Container newContainer = ctx.bodyAsClass(Container.class); //берет из тесла запроса json и сериализует в обьект данного класса
                            newContainer.setChannel(channelRepository.findChannelById(idChannel)); //сохраняем ключ канала в сообщение
                            newContainer.setAuthor(currentUser); //сохраняем ключ автора в сообщение
                            
                            ContainerDto containerDto = new ContainerDto(containerRepository.saveContainer(newContainer));
                            
                            ctx.status(201).json(containerDto);
                            
                            break;
                        }
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
