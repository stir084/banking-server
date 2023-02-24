package com.loose.bankingserver.repository;

import com.loose.bankingserver.model.FriendRequest
import com.loose.bankingserver.model.FriendRequestStatus;
import com.loose.bankingserver.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findByRequesterAndRequested(Member from, Member to);
}