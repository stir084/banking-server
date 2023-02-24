package com.loose.bankingserver.repository;

import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByFrom(Member from);
    List<Friend> findAllByTo(Member to);
    boolean existsByFromAndTo(Member from, Member to);

    Optional<Friend> findByFromAndTo(Member from, Member to);
}