package com.service.cicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CiCdController {
    @GetMapping("/cicd")
    public String cicd(){
        return "hello word!!!";
    }
}
