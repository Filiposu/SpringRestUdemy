package com.restcourse.excepitions;

public class UserServiceException extends RuntimeException{

    public UserServiceException(String message) {
        super(message);
    }
}
