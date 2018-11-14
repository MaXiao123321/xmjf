package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("account")
public class AccountController {

    @RequestMapping("index")
    @RequireLogin
    public  String index(){
        return "account";
    }
}
