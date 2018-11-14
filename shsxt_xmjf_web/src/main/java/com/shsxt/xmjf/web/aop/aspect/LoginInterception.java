package com.shsxt.xmjf.web.aop.aspect;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.NoLoginException;
import com.shsxt.xmjf.api.model.UserModel;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;

/**
 * 定义一个切面做拦截
 */
@Component
@Aspect
public class LoginInterception {

    @Resource
    private HttpSession session;

    @Pointcut("@annotation(com.shsxt.xmjf.web.aop.annotations.RequireLogin)")
    public void cut(){}

    @Before(value = "cut()")
    public void before(){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        if(null==userModel){
            throw new NoLoginException(XmjfConstant.NO_LOGIN_MSG);
        }
    }

}
