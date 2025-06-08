/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.service;

import com.mycompany.stosjavalin.dto.UserDto;
import com.mycompany.stosjavalin.entity.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранит статические данные (о количестве элементов для пагинации и тд...)
 * и также общие обьединяющие вспомогательные методы для контроллеров
 * @author User
 */
public class ConfigData {
    //переменная для пагинации в репозиториях сущностей
    public static final int MIN_AMOUNT_OF_ELEMENTS_FOR_PAGINATION = 30;
    
    /**
     * Метод для преобразования обьектов сущностей пользователей
     * с чувствительными данными в обьекты пользователей без чувствительных данных
     * (логина и пароля)
     * @param users
     * @return 
     */
    public static List<UserDto> translateUserToUserDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
            for (User user : users) {
                usersDto.add(new UserDto(user));
            }
            
        return usersDto;    
    }
}
