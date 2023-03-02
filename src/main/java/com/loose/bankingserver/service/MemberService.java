package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.exception.MemberAlreadyExistsException;
import com.loose.bankingserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.loose.bankingserver.web.dto.MemberDto;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void createMember(MemberDto memberDto) {
        if (isExistingMember(memberDto.getName())) {
            throw new MemberAlreadyExistsException("이미 존재하는 회원입니다.");
        }

        Member member = Member.createMember(memberDto.getName(), memberDto.getPassword());
        memberRepository.save(member);
    }

    public boolean isExistingMember(String name) {
        Optional<Member> foundMember = memberRepository.findByName(name);
        return foundMember.isPresent();
    }

    @Transactional
    public boolean login(String name, String password, HttpSession session) {
        Member member = memberRepository.findByNameAndPassword(name, password);
        if (member == null) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
        } else {
            session.setAttribute("name", member.getName());

            return true;
        }
    }
}