package com.shsxt.xmjf.web.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @ModelAttribute
    public  void preMethod(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
    }
}
