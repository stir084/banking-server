package com.loose.bankingserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.FriendRepository;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.FriendDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendService friendService;


    @Test
    @DisplayName("친구 추가 - 회원이 존재하지 않을 경우")
    void addFriend_memberNotFound() {
        // given
        String username = "john";
        String friendName = "jane";

        when(memberRepository.findByName(username)).thenReturn(Optional.of(new Member(username, "1234")));
        when(memberRepository.findByName(friendName)).thenReturn(Optional.empty());

        // then
        assertThrows(MemberNotFoundException.class, () -> friendService.addFriend(username, friendName));

        // verify
        verify(memberRepository, times(2)).findByName(anyString());
        verify(friendRepository, never()).save(any(Friend.class));
    }


    @Test
    @DisplayName("친구 추가 - 이미 친구인 경우")
    void addFriend_alreadyFriend() {
        // given
        String username = "john";
        String friendName = "jane";
        Member john = new Member("john", "1234");
        Member jane = new Member("jane", "1234");
        Friend friend = Friend.createFriend(john, jane);

        when(memberRepository.findByName(anyString())).thenReturn(Optional.of(john), Optional.of(jane));

        // then
        FriendAlreadyExistsException exception = assertThrows(FriendAlreadyExistsException.class, () -> friendService.addFriend(username, friendName));
        assertEquals("이미 친구입니다.", exception.getMessage());

    }


    @Test
    @DisplayName("친구 추가 - 친구 추가에 성공한 경우")
    void addFriend_success() {
        // given
        String username = "john";
        String friendName = "jane";
        Member john = new Member("john", "1234");
        Member jane = new Member("jane", "1234");

        when(memberRepository.findByName(anyString())).thenReturn(Optional.of(john), Optional.of(jane));
        when(friendRepository.findByMemberAndFriend(any(Member.class), any(Member.class))).thenReturn(Optional.empty());

        // when
        friendService.addFriend(username, friendName);

        // then
        verify(memberRepository, times(2)).findByName(anyString());
        verify(friendRepository, times(1)).findByMemberAndFriend(any(Member.class), any(Member.class));
        verify(friendRepository, times(1)).save(any(Friend.class));
    }


    @Test
    @DisplayName("친구 목록 조회 - 친구가 없는 경우")
    void getFriends_noFriend() {
        // given
        String username = "john";
        Member john = new Member("john", "1234");

        when(memberRepository.findByName(anyString())).thenReturn(Optional.of(john));

        // when
        List<FriendDto> friends = friendService.getFriends(username);

        // then
        assertEquals(0, friends.size());

    }


    @DisplayName("친구 목록 조회 - 친구가 있는 경우")
    @Test
    void getFriends_withFriend() {
        // given
        String username = "john";
        Member john = new Member("john", "1234");
        Member jane = new Member("jane", "1234");
        Friend friend = Friend.createFriend(john, jane);

        when(memberRepository.findByName(anyString())).thenReturn(Optional.of(john));

        // when
        List<FriendDto> friends = friendService.getFriends(username);

        // then
        assertEquals(1, friends.size());
        assertEquals(jane.getName(), friends.get(0).getUsername());

    }
}