package com.example.demo.exceptions;

public class UnauthorizedAccess extends RuntimeException{
    public UnauthorizedAccess(String message) {
        super(message);
    }
}
