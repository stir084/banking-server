package com.loose.bankingserver.repository;

import com.loose.bankingserver.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    Member findByNameAndPassword(String id, String password);
}