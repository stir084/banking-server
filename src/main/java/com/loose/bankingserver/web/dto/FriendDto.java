package com.loose.bankingserver.web.dto;

import com.loose.bankingserver.model.Friend;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FriendDto {
    private String username;

    public static List<FriendDto> toDto(List<Friend> friends) {
        List<FriendDto> friendDtos = new ArrayList<>();
        for (Friend friend : friends) {
            FriendDto friendDto = new FriendDto();
            friendDto.setUsername(friend.getFriend().getName());
            friendDtos.add(friendDto);
        }
        return friendDtos;
    }
}