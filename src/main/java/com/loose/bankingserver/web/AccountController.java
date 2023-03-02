package com.loose.bankingserver.web;

import com.loose.bankingserver.service.AccountService;
import com.loose.bankingserver.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/api/v1/accounts")
    public List<AccountDto> getMyAccounts(HttpSession session) {
        String name = (String) session.getAttribute("name");
        List<AccountDto> accounts = accountService.getMyAccounts(name);

        return accounts;
    }

    @PostMapping("/api/v1/{senderName}/transfer/{receiverName}")
    public ResponseEntity<?> transfer(
            @PathVariable("senderName") String senderName,
            @PathVariable("receiverName") String receiverName,
            @RequestParam("amount") long amount
    ) {
        accountService.transfer(senderName, receiverName, amount);
        return ResponseEntity.ok("이체가 완료되었습니다.");
    }
}