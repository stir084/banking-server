package com.loose.bankingserver.web;

import com.loose.bankingserver.service.AccountService;
import com.loose.bankingserver.web.dto.AccountDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "자신의 계좌를 조회한다.")
    @GetMapping("/api/v1/accounts")
    public List<AccountDto> getMyAccounts(HttpSession session) {
        String name = (String) session.getAttribute("name");
        List<AccountDto> accounts = accountService.getMyAccounts(name);

        return accounts;
    }

    @ApiOperation(value = "상대방에게 돈을 이체한다.")
    @PostMapping("/api/v1/{senderName}/transfer/{receiverName}")
    public ResponseEntity<?> transfer(
            @PathVariable("senderName") String senderName,
            @PathVariable("receiverName") String receiverName,
            @RequestParam("amount") long amount
    ) throws InterruptedException {
        accountService.transfer(senderName, receiverName, amount);
        return ResponseEntity.ok("이체가 완료되었습니다.");
    }
}