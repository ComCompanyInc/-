/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.controller;

import com.mycompany.stosjavalin.StosJavalin;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.repository.UserRepository;
import com.mycompany.stosjavalin.security.JwtSimple;
import io.javalin.Javalin;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author maxim
 */
public class UserController {
    private final Javalin javalin;
    
    public UserController(Javalin javalin, SessionFactory sessionFactory) {
        this.javalin = javalin;
        configureRoutes(sessionFactory);
    }
    
    private void configureRoutes(SessionFactory sessionFactory) {
        UserRepository userRepository = new UserRepository(sessionFactory);
        
        javalin.get("/users", ctx -> {
            String authHeader = ctx.header("Authorization");
           
                //System.out.println("UourTocken(login) = " + authHeader);

                //System.out.println("tocDec = "+JwtSimple.extractLogin(authHeader.substring(7)));

                //ctx.json(authHeader.substring(7) + " " + JwtSimple.getJwtSimple().checkToken(authHeader.substring(7)));
                
            if (authHeader != null && JwtSimple.checkToken(authHeader.substring(7))) {
                ctx.json(userRepository.getAllUsers());
            } else {
                ctx.json("Вы неавторизированны в системе!");
            }
        });
        
        //проверка
        javalin.post("/login", ctx -> { // передаем на авторизацию для получения токена login и password
            String login = ctx.formParam("login");
            String password = ctx.formParam("password");

            System.out.println("login -> " + login);
            System.out.println("password -> " + password);
            System.out.println("Hashed password = " + BCrypt.hashpw(password, BCrypt.gensalt()));
            
            // 1. Находим пользователя в БД
            User user = userRepository.getUserByLogin(login);
            if (user == null) {
                ctx.status(401).result("Неверный логин или пароль");
                return;
            }

            // 2. Проверяем пароль (используем BCrypt)
            boolean isPasswordValid = BCrypt.checkpw(password, user.getPassword());
            if (!isPasswordValid) {
                ctx.status(401).result("Неверный логин или пароль");
                return;
            }

            // 3. Генерируем токен и отдаём
            String token = JwtSimple.createToken(login);
            ctx.json(Map.of("token", token));
        });
        
        javalin.post("/users", ctx -> {
            // 1. Парсим JSON в объект User
            User newUser = ctx.bodyAsClass(User.class);
            
            // 2. Сохраняем обьект в БД
            User user = userRepository.saveUser(newUser);
            ctx.json(user); // Автоматически в JSON
        });
        
        javalin.get("/users/{id}", ctx -> {
            long userId = Long.parseLong(ctx.pathParam("id"));
            User user = userRepository.getUserById(userId);
            ctx.json(user); // Автоматически в JSON
        });
        
        javalin.put("/users", ctx -> {
            // 1. Парсим JSON в объект User
            User updateUser = ctx.bodyAsClass(User.class);
            
            // 2. Сохраняем обьект в БД
            User user = userRepository.updateUser(updateUser);
            ctx.json(user); // Автоматически в JSON
        });
        
        javalin.delete("/users/{id}", ctx -> {
            long userId = Long.parseLong(ctx.pathParam("id"));
            User user = userRepository.deleteUser(userId);
            ctx.json(user);
        });
        
        javalin.patch("/users/{id}", ctx -> {
            // Получаем ID из URL
            long id = Long.parseLong(ctx.pathParam("id"));

            // Парсим JSON тело запроса в Map
            Map<String, Object> updates = ctx.bodyAsClass(Map.class);
            
            // Достаём пользователя из БД
            User existingUser = userRepository.getUserById(id);

            // Частичное обновление полей
            if (updates.containsKey("id")) {
                existingUser.setId(((Integer) updates.get("id")).longValue());
            }
            if (updates.containsKey("code")) {
                existingUser.setCode((String) updates.get("code"));
            }
            if (updates.containsKey("firstName")) {
                existingUser.setFirstName((String) updates.get("first_name"));
            }
            if (updates.containsKey("lastName")) {
                existingUser.setLastName((String) updates.get("last_name"));
            }
            if (updates.containsKey("about")) {
                existingUser.setAbout((String) updates.get("about"));
            }
            if (updates.containsKey("isBlocked")) {
                existingUser.setIsBlocked((boolean) updates.get("isBlocked"));
            }
            if (updates.containsKey("registrationDate")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                
                existingUser.setRegistrationDate(format.parse((String) updates.get("registrationDate")));
            }
            
            // Сохраняем изменения
            User updatedUser = userRepository.updateUser(existingUser);
            ctx.json(updatedUser);
        });
    }
}
