package com.mslog.mslog.controller;

import com.mslog.mslog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        // MethodArgumentNotValidException
            ErrorResponse response =  ErrorResponse.builder()
                    .code("400")
                    .message("잘못된 요청입니다")
                    .build();
            for (FieldError fieldError : e.getFieldErrors()){
                response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return response;
    }
}
