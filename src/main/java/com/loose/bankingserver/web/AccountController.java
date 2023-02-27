package com.loose.bankingserver.web;

import com.loose.bankingserver.repository.AccountRepository;
import com.loose.bankingserver.service.AccountService;
import com.loose.bankingserver.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDto> getMyAccounts(HttpSession session) {
        // 로그인한 사용자의 정보를 세션에서 가져옴
        String name = (String) session.getAttribute("name");
        // 회원의 모든 계좌 정보를 조회
        List<AccountDto> accounts = accountService.getMyAccounts(name);

        return accounts;
    }
}