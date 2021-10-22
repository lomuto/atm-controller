package com.lomuto.exception;

public class CardAlreadyInsertedException extends ATMException{
    public CardAlreadyInsertedException() {
        super(new String("Card already inserted"));
    }
}
