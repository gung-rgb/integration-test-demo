package com.example.demo;


import org.springframework.web.bind.annotation.RestController;

@RestController("ccck")
public class InClassB extends SuperClassA {
    @Override
    public void f() {
        super.f();
    }
}
