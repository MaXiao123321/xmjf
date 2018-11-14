package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.IAccountService;
import com.shsxt.xmjf.api.service.IBasItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("item")
public class BasItemController extends BaseController {

    @Resource
    private IBasItemService basItemService;

    @Resource
    private IAccountService accountService;

    @RequestMapping("list")
    @ResponseBody
    private PageInfo<Map<String,Object>> queryItemsByParams(BasItemQuery basItemQuery){
        return basItemService.queryItemsByParams(basItemQuery);
    }


    @RequestMapping("index")
    public String index(){
        return "invest_list";
    }


    @RequestMapping("updateBasItemStatusToOpen")
    @ResponseBody
    public ResultInfo updateBasItemStatusToOpen(Integer itemId){
        basItemService.updateBasItemStatusToOpen(itemId);
        return new ResultInfo("更新成功");
    }


    @RequestMapping("details")
    public String details(Integer itemId, Model model, HttpSession session){
        BasItem basItem = basItemService.queryBasItemByItemId(itemId);
        model.addAttribute("item",basItem);
        //通过从session中获取值判断用于是否登录
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        if(null != userModel){
            BusAccount busAccount = accountService.queryBusAccountByUserId(userModel.getUserId());
            model.addAttribute("account",busAccount);
        }
        return "details";
    }

}
