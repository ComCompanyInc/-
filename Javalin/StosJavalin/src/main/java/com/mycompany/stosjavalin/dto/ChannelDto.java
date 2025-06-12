/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.dto;

import com.mycompany.stosjavalin.entity.Channel;
import java.util.Date;

/**
 *
 * @author maxim
 */
public class ChannelDto {
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    
    public ChannelDto(Channel channel) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.description = channel.getDescription();
        this.creationDate = channel.getCreationDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
