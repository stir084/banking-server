package com.loose.bankingserver.model;


import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Friend createFriend(Member me, Member you) {
        Friend newFriend = new Friend();
        newFriend.setMember(me);
        newFriend.setFriend(you);
        me.getFriends().add(newFriend);

        return newFriend;
    }
}