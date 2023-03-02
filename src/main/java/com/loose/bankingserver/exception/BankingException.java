package com.loose.bankingserver.exception;

public class BankingException extends RuntimeException {
    private final String errorMessage;

    public BankingException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}