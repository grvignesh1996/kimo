package com.kimo.assignment.Exception;

public class RequestParamNotValidException extends RuntimeException {
    public RequestParamNotValidException(String message){
        super(message);
    }
}

