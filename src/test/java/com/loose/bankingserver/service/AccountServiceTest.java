package com.loose.bankingserver.service;


import com.loose.bankingserver.exception.BalanceNotEnoughException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.exception.NotFriendsException;
import com.loose.bankingserver.model.Account;
import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.AccountRepository;
import com.loose.bankingserver.repository.FriendRepository;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.AccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("내 계좌 조회 테스트")
    void testGetMyAccounts() throws MemberNotFoundException {
        // given
        Member member = new Member("juhno","1234");
        Account account1 = Account.createAccount("123456789", 10000L, member);
        List<Account> accounts = Arrays.asList(account1);

        when(memberRepository.findByName(member.getName())).thenReturn(Optional.of(member));
        when(accountRepository.findByMember(member)).thenReturn(accounts);

        // when
        List<AccountDto> accountDtos = accountService.getMyAccounts(member.getName());

        // then
        assertEquals(1, accountDtos.size());
        assertEquals("123456789", accountDtos.get(0).getAccountNumber());
        assertEquals(10000L, accountDtos.get(0).getBalance());
    }

    @Test
    @DisplayName("계좌 이체 성공 테스트")
    void testTransferSuccess() throws MemberNotFoundException,
            BalanceNotEnoughException, NotFriendsException {
        // given
        Member sender = new Member("juhno", "1234");
        Member receiver = new Member("loose", "1234");
        Account account1 = Account.createAccount("123456789", 10000L, sender);
        Account account2 = Account.createAccount("987654321", 10000L, receiver);

        when(memberRepository.findByName(sender.getName())).thenReturn(Optional.of(sender));
        when(memberRepository.findByName(receiver.getName())).thenReturn(Optional.of(receiver));
        when(accountRepository.findByMember(sender)).thenReturn(Collections.singletonList(account1));
        when(accountRepository.findByMember(receiver)).thenReturn(Collections.singletonList(account2));
        when(friendRepository.findByMemberAndFriend(sender, receiver)).thenReturn(Optional.of(Friend.createFriend(sender, receiver)));

        // when
        accountService.transfer(sender.getName(), receiver.getName(), 5000L);

        // then
        assertEquals(5000L, account1.getBalance());
        assertEquals(15000L, account2.getBalance());
    }

    @Test
    @DisplayName("친구 관계가 아닌 경우 계좌 이체 실패 테스트")
    void testTransferNotFriends() throws MemberNotFoundException {
        // given
        Member sender = new Member("juhno", "1234");
        Member receiver = new Member("loose", "1234");

        when(memberRepository.findByName(sender.getName())).thenReturn(Optional.of(sender));
        when(memberRepository.findByName(receiver.getName())).thenReturn(Optional.of(receiver));
        when(friendRepository.findByMemberAndFriend(sender, receiver)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFriendsException.class, () -> accountService.transfer(sender.getName(), receiver.getName(), 5000L));
    }

    @Test
    @DisplayName("멤버가 존재하지 않는 경우 계좌 이체 실패 테스트")
    void testTransferAccountNotFound() {
        // given
        String senderName = "juhno";
        String receiverName = "loose";

        when(memberRepository.findByName(senderName)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> accountService.transfer(senderName, receiverName, 5000L));
    }

    @Test
    @DisplayName("잔액 부족으로 송금에 실패하는 경우 처리 - BalanceNotEnoughException이 발생해야 함")
    void testTransferBalanceNotEnough() {
        // given
        Member sender = new Member("juhno", "1234");
        Member receiver = new Member("loose", "1234");

        Account senderAccount = Account.createAccount("1234", 10000L, sender);
        Account receiverAccount = Account.createAccount("5678", 0L, receiver);

        Friend friend = Friend.createFriend(sender, receiver);

        when(memberRepository.findByName(sender.getName())).thenReturn(Optional.of(sender));
        when(memberRepository.findByName(receiver.getName())).thenReturn(Optional.of(receiver));
        when(friendRepository.findByMemberAndFriend(sender, receiver)).thenReturn(Optional.of(friend));
        when(accountRepository.findByMember(sender)).thenReturn(List.of(senderAccount));
        when(accountRepository.findByMember(receiver)).thenReturn(List.of(receiverAccount));

        // when, then
        assertThrows(BalanceNotEnoughException.class, () -> {
            accountService.transfer("juhno", "loose", 20000L);
        });

        verify(memberRepository, times(1)).findByName(sender.getName());
        verify(memberRepository, times(1)).findByName(receiver.getName());
        verify(friendRepository, times(1)).findByMemberAndFriend(sender, receiver);
        verify(accountRepository, times(1)).findByMember(sender);
        verify(accountRepository, times(1)).findByMember(receiver);
        verify(accountRepository, never()).save(any(Account.class));
    }
}