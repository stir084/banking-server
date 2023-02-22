package com.loose.bankingserver.repository;

import com.loose.bankingserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}