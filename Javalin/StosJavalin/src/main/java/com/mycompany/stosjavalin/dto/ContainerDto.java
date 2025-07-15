/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.dto;

import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.Container;
import com.mycompany.stosjavalin.entity.User;
import java.util.Date;

/**
 *
 * @author maxim
 */
public class ContainerDto {
    private Long id;
    private ChannelDto channel;
    private Long notes;
    private String description;
    private Date dateSending;
    private boolean isDeleted;
    private UserDto author;
    
    public ContainerDto(Container container) {
        this.id = container.getId();
        this.channel = new ChannelDto(container.getChannel());
        this.notes = container.getNotes();
        this.description = container.getDescription();
        this.dateSending = container.getDateSending();
        this.isDeleted = container.getIsDeleted();
        this.author = new UserDto(container.getAuthor());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChannelDto getChannel() {
        return channel;
    }

    public void setChannel(ChannelDto channel) {
        this.channel = channel;
    }

    public Long getIdNotes() {
        return notes;
    }

    public void setIdNotes(Long idNotes) {
        this.notes = idNotes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateSending() {
        return dateSending;
    }

    public void setDateSending(Date dateSending) {
        this.dateSending = dateSending;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }
}
