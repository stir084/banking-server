package com.loose.bankingserver.repository;

import com.loose.bankingserver.model.Account;
import com.loose.bankingserver.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByMember(Member member);
}