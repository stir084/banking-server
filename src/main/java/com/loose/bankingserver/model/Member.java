package com.loose.bankingserver.model;

import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new ArrayList<>();




    public Member(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public static Member createMember(String name, String password) {
        return new Member(name, password);
    }



}