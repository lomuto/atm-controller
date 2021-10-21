package com.lomuto.atm_exception;

public class ATMException extends Exception{
    private ATMErrorCode ATMErrorCode;

    public ATMException(ATMErrorCode ATMErrorCode) {
        this.ATMErrorCode = ATMErrorCode;
    }

    public ATMErrorCode getErrorCode() {
        return this.ATMErrorCode;
    }
}
