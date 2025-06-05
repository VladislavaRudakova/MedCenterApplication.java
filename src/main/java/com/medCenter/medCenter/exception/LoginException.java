package com.medCenter.medCenter.exception;

public class LoginException extends Exception{

    public LoginException() {
        super("Access denied. Login or password is incorrect");
    }
}
