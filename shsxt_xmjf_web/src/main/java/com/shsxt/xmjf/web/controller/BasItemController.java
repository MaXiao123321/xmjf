package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.*;
import com.shsxt.xmjf.api.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("item")
public class BasItemController extends BaseController {

    @Resource
    private IBasItemService basItemService;

    @Resource
    private IAccountService accountService;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBusItemLoanService busItemLoanService;

    @Resource
    private ISysPictureService sysPictureService;

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

        //贷款及基本信息展示
        Integer loanUserId = basItem.getItemUserId();
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(loanUserId);
        basUserSecurity.setRealname(basUserSecurity.getRealname().substring(0,1)+replaceAll(basUserSecurity.getRealname().substring(1)));
        basUserSecurity.setIdentifyCard(basUserSecurity.getIdentifyCard().substring(0,4)+replaceAll(basUserSecurity.getIdentifyCard().substring(4,14))+basUserSecurity.getIdentifyCard().substring(14));
        model.addAttribute("loanUser",basUserSecurity);

        model.addAttribute("busItemLoan",busItemLoanService.queryBusItemLoanByItemId(basItem.getId()));
        List<Map<String, Object>> pics = sysPictureService.querySysPicturesByItemId(itemId);
        model.addAttribute("pics",pics);
        return "details";
    }


    public static String replaceAll(String str){
        StringBuffer stringBuffer = new StringBuffer();
        if(StringUtils.isBlank(str)){
            return null;
        }
        for (int i = 0;i<str.length();i++){
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

}
