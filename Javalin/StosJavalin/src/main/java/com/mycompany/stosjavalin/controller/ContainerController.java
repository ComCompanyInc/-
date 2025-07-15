/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.dto.ContainerDto;
import com.mycompany.stosjavalin.entity.Container;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.repository.ContainerRepository;
import com.mycompany.stosjavalin.repository.UserChannelRepository;
import com.mycompany.stosjavalin.service.ConfigData;
import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.List;
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
    //                    System.out.println("userId: " + user.getId());
    //                    System.out.println("userName: " + user.getFirstName());
    //                    System.out.println("userSurname: " + user.getLastName());
    //                    System.out.println("__________________________________");

                        if (user.getId() == currentUser.getId()) { //если полученные id совпадают, выводим контейнеры выбранного канала
                            containerResults = containerRepository.findContainersForChannel(channelId, paginationPage, searchContainersFilter);
                        }
                    }

                    List<ContainerDto> containersDto = new ArrayList<ContainerDto>();
                    for (Container container : containerResults) {
                        containersDto.add(new ContainerDto(container));
                    }

                    ctx.json(containersDto);
                } else {
                    ctx.json("Ошибка: Доступ заблокирован (пользоватебль пустой!)");
                }
                
            } else {
                ctx.json("Ошибка: пользоватебль пустой!");
            }
        });
    }
}
