package com.shsxt.xmjf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @GetMapping("login")
    public String login(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        System.out.println(request.getContextPath());
        return "login";
    }

    @GetMapping("register")
    public  String register(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "register";
    }

    @GetMapping("index")
    public  String index(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "index";
    }

    @GetMapping("quickLogin")
    public  String quickLogin(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "quick_login";
    }

}
