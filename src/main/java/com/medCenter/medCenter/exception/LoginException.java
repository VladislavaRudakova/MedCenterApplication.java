package com.medCenter.medCenter.exception;

public class LoginException extends Exception{

    public LoginException() {
        super("Login already exists");
    }
}
