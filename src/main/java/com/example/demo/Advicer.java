package com.example.demo;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class Advicer {
    @ExceptionHandler(Exception.class)
    public Map map(Exception e, HttpServletRequest request){

        System.out.println("adsf");
        return new HashMap();
    }
}
