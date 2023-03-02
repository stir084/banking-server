package com.loose.bankingserver.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String name;
    private String password;

    public MemberDto(){

    }
    public MemberDto(String name, String password){
        this.name = name;
        this.password = password;
    }
}