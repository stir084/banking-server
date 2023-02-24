package com.loose.bankingserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;


    private Member(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static Member createMember(String name, String password) {
        return new Member(name, password);
    }
    // 생성자, getter/setter 생략
}