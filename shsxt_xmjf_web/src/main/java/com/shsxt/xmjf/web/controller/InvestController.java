package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;
import com.shsxt.xmjf.api.service.IBusItemInvestService;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("invest")
public class InvestController {

    @Resource
    private IBusItemInvestService busItemInvestService;

    @RequestMapping("list")
    @ResponseBody
    public PageInfo<Map<String,Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery) {
        return busItemInvestService.queryInvestItemsByItemId(itemInvestQuery);

    }

    @RequestMapping("doInvest")
    @ResponseBody
    @RequireLogin
    public ResultInfo doInvest(Integer itemId, BigDecimal amount, String busiPassword, HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        busItemInvestService.addBusItemInvest(itemId,amount,userModel.getUserId(),busiPassword);
        return new ResultInfo("投资成功!");
    }


    @RequestMapping("countInvestIncomeInfoByUserId")
    @ResponseBody
    @RequireLogin
    public Map<String,Object> countInvestIncomeInfoByUserId(HttpSession session){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        return busItemInvestService.countInvestIncomeInfoByUserId(userModel.getUserId());
    }

}
