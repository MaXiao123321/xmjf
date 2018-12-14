package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.apache.zookeeper.server.SessionTracker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

//@Controller
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("user/{userId}")
    @ResponseBody
    public User queryUserByUserId(@PathVariable Integer userId){
        return userService.queryUserByUserId(userId);
    }


    @RequestMapping("user/register")
    @ResponseBody
    public ResultInfo register(String phone,String password,String code){
        ResultInfo resultInfo = new ResultInfo();
        try {
            userService.saveUser(phone,password,code);
        } catch (BusiException e){
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
        }
        return resultInfo;
    }


    @PostMapping("user/quickLogin")
    @ResponseBody
    public ResultInfo quickLogin(String phone, String code, HttpSession session){
        ResultInfo resultInfo = new ResultInfo();
        try {
            UserModel userModel = userService.quickLogin(phone, code);
            session.setAttribute(XmjfConstant.SESSION_USER,userModel);
        } catch (BusiException e){
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
        }
        return resultInfo;
    }


    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo login(String phone,String password,HttpSession session){
        ResultInfo resultInfo = new ResultInfo();
        try {
            UserModel login = userService.login(phone, password);
            session.setAttribute(XmjfConstant.SESSION_USER,login);
        } catch (BusiException e){
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
        }
        return resultInfo;
    }


    @RequestMapping("user/auth")
    @ResponseBody
    @RequireLogin
    public ResultInfo userAuth(String realName,String cardNo,String busiPassword,HttpSession session){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        return userService.updateBasUserSecurityInfo(realName,cardNo,userModel.getUserId(),busiPassword);
    }


}
