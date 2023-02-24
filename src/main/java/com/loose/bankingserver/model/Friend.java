package com.loose.bankingserver.model;


import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import lombok.Getter;

import javax.persistence.*;


@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private Member friend;

    public Friend(Member from, Member to) {
    }

    public Friend() {

    }

    // 생성자, getter, setter 생략
}