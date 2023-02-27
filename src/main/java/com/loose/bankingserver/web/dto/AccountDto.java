package com.loose.bankingserver.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private String accountNumber;
    private Long balance;

    public AccountDto(String accountNumber, Long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // 생성자, getter, setter 생략
}