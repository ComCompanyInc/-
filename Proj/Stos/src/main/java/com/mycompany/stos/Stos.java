/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.stos;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author maxim
 */
public class Stos {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        // Загрузка контекста Spring
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
