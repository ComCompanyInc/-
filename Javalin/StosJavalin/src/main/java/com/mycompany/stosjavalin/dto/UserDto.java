/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mycompany.stosjavalin.entity.User;
import java.util.Date;

/**
 * Класс для вывода в контроллере только тех значений которые должен видеть пользователь,
 * а не все, которые есть в сущности User
 * @author User
 */
public class UserDto {
    private Long id;
    private String code;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;
    private String about;
    private boolean isBlocked;

    // Конструктор, который копирует ВСЕ нужные поля из User
    public UserDto(User user) {
        this.id = user.getId();
        this.code = user.getCode();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.registrationDate = user.getRegistrationDate();
        this.about = user.getAbout();
        this.isBlocked = user.getIsBlocked();
    }

    // Геттеры (сеттеры можно не добавлять, если DTO read-only)
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Date getRegistrationDate() { return registrationDate; }
    public String getAbout() { return about; }
    public boolean getIsBlocked() { return isBlocked; }
}
