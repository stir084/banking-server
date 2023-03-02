package com.loose.bankingserver.web;

import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.service.MemberService;
import com.loose.bankingserver.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members/signup")
    public ResponseEntity<Void> signUp(@RequestBody MemberDto memberDto) {
        memberService.createMember(memberDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/members/login")
    public ResponseEntity<Object> login(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session) {
        boolean result = memberService.login(name, password, session);
        if (result) {
            return new ResponseEntity<>("로그인을 성공하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/v1/members/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
