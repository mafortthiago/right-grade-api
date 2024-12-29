package com.mafort.rightgrade.infra.exception;

public class InvalidArgumentException extends RuntimeException{
    public InvalidArgumentException(String message){
        super(message);
    }
}
