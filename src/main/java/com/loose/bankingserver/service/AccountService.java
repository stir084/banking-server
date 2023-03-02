package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.MemberAlreadyExistsException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Account;
import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.AccountRepository;
import com.loose.bankingserver.repository.FriendRepository;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.AccountDto;
import com.loose.bankingserver.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));
        List<Account> accounts = accountRepository.findByMember(member);

        return accounts.stream()
                .map(account -> new AccountDto(account.getAccountNumber(), account.getBalance()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void transfer(String senderName, String receiverName, Long amount) {

        Member sender = memberRepository.findByName(senderName)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));
        Member receiver = memberRepository.findByName(receiverName)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));

        List<Account> senderAccounts = accountRepository.findByMember(sender);
        List<Account> receiverAccounts = accountRepository.findByMember(receiver);

        if (senderAccounts.isEmpty()) {
            throw new IllegalArgumentException("Sender does not have an account");
        }

        if (receiverAccounts.isEmpty()) {
            throw new IllegalArgumentException("Receiver does not have an account");
        }

        Account senderAccount = senderAccounts.get(0); // 첫 번째 계좌 사용
        Account receiverAccount = receiverAccounts.get(0); // 첫 번째 계좌 사용

        Friend friend = friendRepository.findByMemberAndFriend(sender, receiver)
                .orElseThrow(() -> new IllegalArgumentException("Sender and receiver are not friends"));

        if (senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
    }
}