package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.AcoountNotFoundException;
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
    public void transfer(String senderName, String receiverName, Long amount) {

        Member sender = memberRepository.findByName(senderName)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
        Member receiver = memberRepository.findByName(receiverName)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

        List<Account> senderAccounts = accountRepository.findByMember(sender);
        List<Account> receiverAccounts = accountRepository.findByMember(receiver);

        if (senderAccounts.isEmpty()) {
            throw new AcoountNotFoundException("송금자의 계좌가 존재하지 않습니다.");
        }

        if (receiverAccounts.isEmpty()) {
            throw new AcoountNotFoundException("수취인의 계좌가 존재하지 않습니다.");
        }

        Account senderAccount = senderAccounts.get(0);
        Account receiverAccount = receiverAccounts.get(0);

        friendRepository.findByMemberAndFriend(sender, receiver)
                .orElseThrow(() -> new NotFriendsException("송금자와 수취인이 친구가 아닙니다."));

        if (senderAccount.getBalance() < amount) {
            throw new BalanceNotEnoughException("송금자의 계좌의 잔액이 부족합니다.");
        }

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
    }
}