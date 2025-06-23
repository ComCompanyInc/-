/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name = "user_channel")
public class UserChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne //(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne //(cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_id")
    private Channel channel;
    
    @Column(name = "is_active_user_for_group")
    private boolean isActiveUserForGroup = true;

    @Column(name = "is_author_user_for_group")
    private boolean isAuthorUserForGroup = false;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public boolean getIsActiveUserForGroup() {
        return isActiveUserForGroup;
    }
    
    public void setIsActiveUserForGroup(boolean isActiveUserForGroup) {
        this.isActiveUserForGroup = isActiveUserForGroup;
    }

    public boolean getIsAuthorUserForGroup() {
        return isAuthorUserForGroup;
    }
    
    public void setIsAuthorUserForGroup(boolean isAuthorUserForGroup) {
        this.isAuthorUserForGroup = isAuthorUserForGroup;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
