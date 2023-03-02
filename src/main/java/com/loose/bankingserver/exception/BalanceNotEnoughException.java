package com.loose.bankingserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BalanceNotEnoughException extends BankingException {
    public BalanceNotEnoughException(String message) {
        super(message);
    }
}