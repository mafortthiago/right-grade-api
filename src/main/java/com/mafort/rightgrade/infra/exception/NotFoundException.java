package com.mafort.rightgrade.infra.exception;
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
