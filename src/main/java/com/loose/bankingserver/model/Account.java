package com.loose.bankingserver.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String accountNumber;

    private Long balance;

    // 생성자, getter, setter 생략
    public static Account createAccount(String accountNumber, Long balance) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);

        return account;
    }

    public void deposit(Long amount) {
        this.balance += amount;
    }

    public boolean withdraw(Long amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
}