package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.MemberAlreadyExistsException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("기존에 존재하는 회원이 아닐 때 회원 생성이 정상적으로 이루어진다.")
    void createMember() {
        // given
        MemberDto memberDto = new MemberDto("junho", "1234");
        when(memberRepository.findByName(memberDto.getName())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(null);

        // when
        memberService.createMember(memberDto);

        // then
        verify(memberRepository, times(1)).findByName(memberDto.getName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("이미 존재하는 회원일 때 MemberAlreadyExistsException이 발생한다.")
    void createMember_whenMemberAlreadyExists() {
        // given
        MemberDto memberDto = new MemberDto("junho", "1234");
        when(memberRepository.findByName(memberDto.getName())).thenReturn(Optional.of(Member.createMember(memberDto.getName(), memberDto.getPassword())));

        // when, then
        assertThrows(MemberAlreadyExistsException.class, () -> memberService.createMember(memberDto));
        verify(memberRepository, times(1)).findByName(memberDto.getName());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("이미 존재하는 회원일 때 true를 반환한다.")
    void isExistingMember_whenMemberExists() {
        // given
        String name = "junho";
        when(memberRepository.findByName(name)).thenReturn(Optional.of(Member.createMember(name, "1234")));

        // when
        boolean result = memberService.isExistingMember(name);

        // then
        assertTrue(result);
        verify(memberRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("존재하지 않는 회원일 때 false를 반환한다.")
    void isExistingMember_whenMemberNotExists() {
        // given
        String name = "junho";
        when(memberRepository.findByName(name)).thenReturn(Optional.empty());

        // when
        boolean result = memberService.isExistingMember(name);

        // then
        assertFalse(result);
        verify(memberRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("회원이 존재하고 비밀번호가 일치할 때 true를 반환한다.")
    void login_whenMemberExists() {
        // given
        String name = "junho";
        String password = "1234";
        HttpSession httpSession = mock(HttpSession.class);
        Member member = Member.createMember(name, password);
        when(memberRepository.findByNameAndPassword(name, password)).thenReturn(member);

        // when
        boolean result = memberService.login(name, password, httpSession);

        // then
        assertTrue(result);
        verify(httpSession, times(1)).setAttribute("name", name);
    }

    @Test
    @DisplayName("회원이 존재하지 않거나 비밀번호가 일치하지 않을 때 MemberNotFoundException이 발생한다.")
    void login_whenMemberNotExists() {
        // given
        String name = "junho";
        String password = "1234";
        HttpSession httpSession = mock(HttpSession.class);
        when(memberRepository.findByNameAndPassword(name, password)).thenReturn(null);

        // when, then
        assertThrows(MemberNotFoundException.class, () -> memberService.login(name, password, httpSession));
        verify(httpSession, never()).setAttribute(anyString(), any());
    }
}