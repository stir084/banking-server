package com.loose.bankingserver.model;


import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@Setter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    public Friend() {}

    public Friend(Member member, Member friend) {
        this.member = member;
        this.friend = friend;
    }

    public boolean areFriends(Member member, Member friend) {
        return this.member.equals(member) && this.friend.equals(friend)
                || this.member.equals(friend) && this.friend.equals(member);
    }
}