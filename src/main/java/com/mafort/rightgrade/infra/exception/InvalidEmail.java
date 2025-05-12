package com.mafort.rightgrade.infra.exception;

public class InvalidEmail extends RuntimeException{
    public InvalidEmail(String message){
        super(message);
    }
}
