package com.loose.bankingserver.model;

import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> Accounts = new ArrayList<>();



  /*  public Member(String name, String password) {
        this.name = name;
        this.password = password;
    }*/


    public static Member createMember(String name, String password, Account... accounts) {

        Member member = new Member();

        member.setName(name);
        member.setPassword(password);
        for (Account account : accounts) {
            member.addAccount(account);
        }
        return member;
    }

    public void addAccount(Account account) {
        Accounts.add(account);
        account.setMember(this);
    }



}