/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author maxim
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "registration_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;
    
    @Column(name = "about")
    private String about;
    
    @Column(name = "is_blocked")
    private boolean isBlocked;
    
    @Column(name = "login")
    private String login;
    
    @Column(name = "password")
    private String password;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<UserChannel> userChannel = new ArrayList<>();
    
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL
    )
    private List<Container> container = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(@JsonFormat(pattern = "yyyy-MM-dd") Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
    
    public boolean getIsBlocked() {
        return isBlocked;
    }
    
    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt()); // кладем хешированный пароль
    }

    public List<UserChannel> getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(List<UserChannel> userChannel) {
        this.userChannel = userChannel;
    }

    public List<Container> getContainer() {
        return container;
    }

    public void setContainer(List<Container> container) {
        this.container = container;
    }
}
