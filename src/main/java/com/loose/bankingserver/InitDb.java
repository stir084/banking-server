package com.loose.bankingserver;

import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Account;
import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 종 주문 2개
 * * userA
 * 	 * JPA1 BOOK
 * 	 * JPA2 BOOK
 * * userB
 * 	 * SPRING1 BOOK
 * 	 * SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {


            Member member1 = Member.createMember("junho", "1234");
            Account.createAccount("123456789", 1000000L, member1);

            em.persist(member1);

            Member member2 = Member.createMember("loose", "1234");
            Account.createAccount("987654321", 1000000L, member2);

            em.persist(member2);

            Friend newFriend1 = Friend.createFriend(member1, member2);
            em.persist(newFriend1);
            Friend newFriend2 = Friend.createFriend(member2, member1);
            em.persist(newFriend2);

        }
    }
}

