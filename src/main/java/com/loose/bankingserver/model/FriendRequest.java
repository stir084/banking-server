package com.loose.bankingserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_id")
    private Member requested;

    public FriendRequest() {}

    public FriendRequest(Member requester, Member requested) {
        this.requester = requester;
        this.requested = requested;
    }

}