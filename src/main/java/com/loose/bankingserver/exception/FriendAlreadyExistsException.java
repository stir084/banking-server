package com.loose.bankingserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FriendAlreadyExistsException extends BankingException {
    public FriendAlreadyExistsException(String message) {
        super(message);
    }
}