package com.mafort.rightgrade.infra.exception;

public class InvalidCodeException extends RuntimeException{
    public InvalidCodeException(String message){
        super(message);
    }
}
