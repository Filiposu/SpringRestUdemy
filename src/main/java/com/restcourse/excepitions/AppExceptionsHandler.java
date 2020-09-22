package com.restcourse.excepitions;

import com.restcourse.service.UserService;
import com.restcourse.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserException(UserServiceException ex, WebRequest webRequest)
    {
        System.out.println("Exception occured inside UserException " + ex.toString());
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setTimestamp(new Date());
        errorMessage.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGlobalException(Exception ex,WebRequest webRequest){
        ex.printStackTrace();
        ErrorMessage message = new ErrorMessage(new Date(),ex.getMessage());
        return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
