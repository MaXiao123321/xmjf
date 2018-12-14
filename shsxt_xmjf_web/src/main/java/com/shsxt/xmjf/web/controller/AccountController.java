package com.shsxt.xmjf.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.service.IAccountService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("account")
public class AccountController extends BaseController {

    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    @Resource
    private IAccountService accountService;


    @RequestMapping("index")
    @RequireLogin
    public  String index(){
        System.out.println("Test");
        return "account_info";
    }


    @RequestMapping("recharge")
    @RequireLogin
    public String recharge(){
        System.out.println("Test");
        return "recharge";
    }

    // 充值记录展示页面
    @RequestMapping("rechargeRecord")
    public String rechargeRecord(){
        return "recharge_record";
    }


    @RequestMapping("countBusAccountInfoByUserId")
    @ResponseBody
    @RequireLogin
    public Map<String,Object> countBusAccountInfoByUserId(HttpSession session){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        return accountService.countBusAccountInfoByUserId(userModel.getUserId());
    }


    @RequestMapping("returnUrl")
    public String returnUrl(@RequestParam(name = "out_trade_no") String orderNo,@RequestParam(name = "total_amount")BigDecimal totalAmount,
                          @RequestParam(name = "seller_id") String sellerId,@RequestParam(name = "trade_no")String busiNo,
                          @RequestParam(name="app_id")String appId, HttpServletRequest request){
        Boolean flag = checkSign(request);
        try {
            AssertUtil.isTrue(!flag,"订单支付异常,请联系客服");
            busAccountRechargeService.updateBusAccountRechargeInfo(orderNo,totalAmount,sellerId,appId,busiNo);
            return "success";
        } catch (BusiException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg",e.getMsg());
            return "failed";
        }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg","订单支付失败!");
            return "failed";
        }
    }


    @RequestMapping("notifyUrl")
    public String notifyUrl(@RequestParam(name = "out_trade_no") String orderNo,@RequestParam(name = "total_amount")BigDecimal totalAmount,
                          @RequestParam(name = "seller_id") String sellerId,@RequestParam(name = "trade_no")String busiNo,
                          @RequestParam(name="app_id")String appId,@RequestParam(name = "trade_status") String tradeStatus,HttpServletRequest request){
        //System.out.println("异步回调通知...");
        /**
         * 页面输出success/fail
         *
         */
        try {
            Boolean flag = checkSign(request);
            AssertUtil.isTrue(!flag,"订单支付异常,请联系客服");
            if(tradeStatus.equals(AlipayConfig.trade_status)){
                busAccountRechargeService.updateBusAccountRechargeInfo(orderNo,totalAmount,sellerId,appId,busiNo);
                request.setAttribute("msg","success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg","fail");
        }
        return "notify";
    }


    public Boolean checkSign(HttpServletRequest request){
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        System.out.println("----------------------");
        for(Map.Entry<String,String> entry:params.entrySet()){
            System.out.println(entry.getKey()+"--"+entry.getValue());
        }
        System.out.println("--------------------------");
        Boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return signVerified;
    }

}
