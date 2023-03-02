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
      //  initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {

            Account account1 = Account.createAccount("123456789", 1000000L);
            Member member1 = Member.createMember("junho", "1234", account1);

            System.out.println(member1.getAccounts().size() + "--");
            em.persist(member1);

            Account account2 = Account.createAccount("987654321", 1000000L);
            Member member2 = Member.createMember("loose", "1234", account2);
            em.persist(member2);


            Friend newFriend1 = new Friend();
            newFriend1.setMember(member1);
            newFriend1.setFriend(member2);
            member1.getFriends().add(newFriend1);
            em.persist(newFriend1);

            Friend newFriend2 = new Friend();
            newFriend2.setMember(member2);
            newFriend2.setFriend(member1);
            member2.getFriends().add(newFriend2);
            em.persist(newFriend2);

        }
    }
}

