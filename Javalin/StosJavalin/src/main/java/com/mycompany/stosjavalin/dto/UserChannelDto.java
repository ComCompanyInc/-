/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stosjavalin.dto;

import com.mycompany.stosjavalin.entity.Channel;
import com.mycompany.stosjavalin.entity.User;
import com.mycompany.stosjavalin.entity.UserChannel;

/**
 *
 * @author maxim
 */
public class UserChannelDto {
    private Long id;
    private ChannelDto channelDto;
    //private User user;
    private boolean isActiveUserForGroup;

    public UserChannelDto(UserChannel userChannel) {
        this.id = userChannel.getId();
        this.channelDto = new ChannelDto(userChannel.getChannel());
        this.isActiveUserForGroup = userChannel.getIsActiveUserForGroup();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChannelDto getChannelDto() {
        return channelDto;
    }

    public void setChannelDto(ChannelDto channelDto) {
        this.channelDto = channelDto;
    }

    public boolean getIsActiveUserForGroup() {
        return isActiveUserForGroup;
    }
    
    public void getIsActiveUserForGroup(boolean isActiveUserForGroup) {
        this.isActiveUserForGroup = isActiveUserForGroup;
    }
}
