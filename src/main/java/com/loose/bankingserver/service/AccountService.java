package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.AccountNotFoundException;
import com.loose.bankingserver.exception.BalanceNotEnoughException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.exception.NotFriendsException;
import com.loose.bankingserver.model.Account;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.AccountRepository;
import com.loose.bankingserver.repository.FriendRepository;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;
    @Transactional
    public List<AccountDto> getMyAccounts(String name) {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
        List<Account> accounts = accountRepository.findByMember(member);

        return accounts.stream()
                .map(account -> new AccountDto(account.getAccountNumber(), account.getBalance()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void transfer(String senderName, String receiverName, Long amount) throws InterruptedException {

        Member sender = memberRepository.findByName(senderName)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
        Member receiver = memberRepository.findByName(receiverName)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

        boolean areFriends = sender.getFriends().stream()
                .anyMatch(friend -> friend.getFriend().equals(receiver))
                && receiver.getFriends().stream()
                .anyMatch(friend -> friend.getFriend().equals(sender));

        if(areFriends == false){
            throw new NotFriendsException("송금자와 수취인이 친구가 아닙니다.");
        }
        List<Account> senderAccounts = sender.getAccounts();
        List<Account> receiverAccounts = receiver.getAccounts();

        if (senderAccounts.isEmpty()) {
            throw new AccountNotFoundException("송금자의 계좌가 존재하지 않습니다.");
        }

        if (receiverAccounts.isEmpty()) {
            throw new AccountNotFoundException("수취인의 계좌가 존재하지 않습니다.");
        }

        Account senderAccount = senderAccounts.get(0);
        Account receiverAccount = receiverAccounts.get(0);


        if (senderAccount.getBalance() < amount) {
            throw new BalanceNotEnoughException("송금자의 계좌의 잔액이 부족합니다.");
        }

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        notify(receiver, "이체가 완료 되었습니다.");
    }

    public void notify(Member member, String message) throws InterruptedException {
        Thread.sleep(500);
    }
}