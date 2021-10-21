package com.lomuto.atm_exception;

public class ATMException extends Exception{
    private ErrorCode errorCode;

    public ATMException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
