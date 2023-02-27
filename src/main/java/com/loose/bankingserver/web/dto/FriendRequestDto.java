package com.loose.bankingserver.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestDto {
    private String requesterUsername;
    private String requestedUsername;
}