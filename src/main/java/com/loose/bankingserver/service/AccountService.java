package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.MemberAlreadyExistsException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Account;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.AccountRepository;
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
    public void transfer(Member sender, Member receiver, Long amount) {
        Account senderAccount = accountRepository.findByMember(sender)
                .orElseThrow(() -> new IllegalArgumentException("Sender does not have an account"));
        Account receiverAccount = accountRepository.findByMember(receiver)
                .orElseThrow(() -> new IllegalArgumentException("Receiver does not have an account"));

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