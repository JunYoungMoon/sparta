package com.sparta.springprepare.prepare.controller;

import com.sparta.springprepare.prepare.Star2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/form/json")
    public String helloPostRequestJson(Star2 star2) {
        return String.format("Hello, @RequestBody.<br> (name = %s, age = %d) ", star2.name, star2.age);
    }
}
