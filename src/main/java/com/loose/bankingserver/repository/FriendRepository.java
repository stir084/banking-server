package com.loose.bankingserver.repository;

import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByMemberAndFriend(Member sender, Member receiver);
}