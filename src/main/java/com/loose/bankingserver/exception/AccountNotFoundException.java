package com.loose.bankingserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}