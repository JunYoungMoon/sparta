package com.service.redis.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @GetMapping("/set")
    public String setSession(@RequestParam("q") String q, HttpSession session) {
        // Tomcat이 세션을 관리하는데 그렇기에 자동으로 생성되는 JSESSIONID 쿠키 또한 Tomcat이 생성하고 있다.
        // 예전에 세션 쿠키의 SameSite나 HttpOnly 설정을 할때도
        // properties에 server.servlet.session.cookie.http-only=true와 같은 설정을 해줬던 기억이 난다 server는 tomcat을 의미한다.
        // 또 properties에 작성하기도 하지만 TomcatConfigure 클래스를 만들어서 조작하기도 했다.
        session.setAttribute("q", q);
        return "save " + session;
    }

    @GetMapping("/get")
    public String getSession(HttpSession session) {
        return String.valueOf(session.getAttribute("q"));
    }
}
