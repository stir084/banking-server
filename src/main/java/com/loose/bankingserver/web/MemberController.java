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
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody MemberDto memberDto) {
        memberService.createMember(memberDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session) {

        Member member = memberRepository.findByNameAndPassword(name, password);
        if (member == null) {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        } else {
            session.setAttribute("name", member.getName());
            return new ResponseEntity<>("Login succeeded", HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @GetMapping("/test")
    public void test(HttpSession session) {
        System.out.println(session.getAttribute("name"));
    }
}
