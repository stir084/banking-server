package com.loose.bankingserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AcoountNotFoundException extends BankingException {
    public AcoountNotFoundException(String message) {
        super(message);
    }
}