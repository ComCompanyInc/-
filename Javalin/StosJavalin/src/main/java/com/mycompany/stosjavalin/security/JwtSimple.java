/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

/**
 * Этот класс просто дешифрует токены по ключу 
 * (ключ генерируется каждый раз при перезапуске сервера новый, поэтому старые токены до ерезапуска становятся невалидными)
 * все токены генерируются на роуте \login по логину и паролю, хранятся на клиенских приложениях
 * и передаются в заголовках запроса для сервера
 * @author User
 */
public class JwtSimple {
    // Секретный ключ (как "пароль" для подписи токенов)
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    /**
     * Создание токена
     * @param username
     * @return 
     */
    public static String createToken(String username) {
        return Jwts.builder() // начинаем строить токен
            .setSubject(username) // кладём внутрь имя пользователя (это как подпись на пропуске)
            .signWith(SECRET_KEY) // подписываем нашим секретным ключом (как печать)
            .compact(); // превращаем в строку (токен готов)
    }
    
    /**
     * Проверка валидности токена
     * @param token
     * @return 
     */
    public static boolean checkToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // проверяем тем же ключом
                .build()
                .parseClaimsJws(token); // если не взорвалось — токен valid
            return true;
        } catch (Exception e) {
            System.out.println("ex=> "+e);
            return false; // если ошибка — токен недействителен
        }
    }
    
    /**
     * Декодирование токена из Middlware для получения из него логина
     * @param token
     * @return 
     */
    public static String extractLogin(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)  // Проверяем подпись тем же ключом
            .build()
            .parseClaimsJws(token)     // Декодируем токен
            .getBody()                 // Достаём "тело" токена (payload)
            .getSubject();             // Берём логин (subject)
    }
}
