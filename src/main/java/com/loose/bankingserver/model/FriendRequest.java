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

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne
    @JoinColumn(name = "requested_id")
    private Member requested;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    public FriendRequest(Member requester, Member requested, FriendRequestStatus pending) {
        this.requester = requester;
        this.requested = requested;
        this.status = pending;
    }
}