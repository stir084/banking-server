package com.loose.bankingserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MemberAlreadyExistsException extends BankingException {
    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}