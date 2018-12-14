package com.shsxt.xmjf.web.alipay;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import com.shsxt.xmjf.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@RequestMapping("alipay")
public class AlipayController extends BaseController {

    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    @RequestMapping("doRecharge")
    @RequireLogin
    public String doRecharge(BigDecimal amount,String busiPassword,String imageCode, HttpServletRequest request){
        UserModel userModel = (UserModel) request.getSession().getAttribute(XmjfConstant.SESSION_USER);
        try {
            AssertUtil.isTrue(StringUtils.isBlank(imageCode),"请输入图片验证码");
            String sessionImageCode = (String) request.getSession().getAttribute(XmjfConstant.SESSION_IMAGE);
            AssertUtil.isTrue(StringUtils.isBlank(sessionImageCode),"当前页面已失效,请重新刷新页面!");
            AssertUtil.isTrue(!(imageCode.equals(sessionImageCode)),"图片验证码不正确!");
            request.getSession().removeAttribute(XmjfConstant.SESSION_IMAGE);
            String result = busAccountRechargeService.addBusAccountRecharge(userModel.getUserId(), amount, busiPassword);
            request.setAttribute("result",result);
        } catch (BusiException e) {
            e.printStackTrace();
            request.setAttribute("msg",e.getMsg());
            return "recharge";
        } catch (Exception e){
            e.printStackTrace();
            request.setAttribute("msg",XmjfConstant.OPS_FAILED_MSG);
            return "recharge";
        }
        return "result";
    }

}
