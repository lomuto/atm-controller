package com.lomuto.exception;

public class AccountAlreadySelectedException extends ATMException {
    public AccountAlreadySelectedException() {
        super(new String("Account has been already selected"));
    }
}
