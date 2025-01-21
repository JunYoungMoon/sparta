package com.sparta.springprepare.prepare.controller;

import com.sparta.springprepare.prepare.Star;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HTMLController {

    @GetMapping("/static-hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/html/redirect")
    public String htmlStatic(){
        return "redirect:/hello.html";
    }

    @GetMapping("/html/templates")
    public String htmlTemplate(){
        return "hello";
    }

    @GetMapping("/html/dynamic")
    public String htmlTemplate(Model model){
        model.addAttribute("visits", 123);
        return "hello-visit";
    }

    @GetMapping("/json/string")
    @ResponseBody
    public String helloStringJson() {
        return "{\"name\":\"Robbie\",\"age\":95}";
    }

    @GetMapping("/json/class")
//    @ResponseBody
    public Star helloClassJson() {
        return new Star("Robbie", 95);
    }
}
