package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("recharge")
public class BusAccountRechargeController extends BaseController {

    @Resource
    private IBusAccountRechargeService busAccountRechargeService;


    @RequestMapping("list")
    @ResponseBody
    @RequireLogin
    public PageInfo<Map<String,Object>> queryRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery, HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        busAccountRechargeQuery.setUserId(userModel.getUserId());
        return busAccountRechargeService.queryRechargesByUserId(busAccountRechargeQuery);
    }

}
