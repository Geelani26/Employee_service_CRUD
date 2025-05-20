package com.example.HRMS.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map<String,String> handleMaxUploadFileSizeError(MaxUploadSizeExceededException ex) {
        Map<String, String> Object = new HashMap<>();
        Object.put("Code", "500");
        Object.put("message", ex.getMessage());
        Object.put("data", " Maximum FileSize Error Occurred");
        return Object;
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> Object = new HashMap<>();
        Object.put("Code", "500");
        Object.put("message", ex.getFieldError().getDefaultMessage());
        Object.put("data", "Validation Error Occurred");
        return Object;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Map<String,String> handleExceptionError(Exception ex) {
        Map<String, String> Object = new HashMap<>();
        Object.put("Code", "400");
        Object.put("message", ex.getMessage());
        Object.put("data", "Unexpected Error Occurred");
        return Object;
    }

}
