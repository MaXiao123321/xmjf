package com.shsxt.xmjf.server.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.enums.OrderStatus;
import com.shsxt.xmjf.api.enums.RechaggeType;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.MD5;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import com.shsxt.xmjf.server.db.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BusAccountRechargeServiceImpl implements IBusAccountRechargeService {

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BusAccountRechargeMapper busAccountRechargeMapper;

    @Resource
    private BusAccountMapper busAccountMapper;

    @Resource
    private BusAccountLogMapper busAccountLogMapper;

    @Resource
    private BusUserStatMapper busUserStatMapper;

    @Resource
    private BusUserIntegralMapper busUserIntegralMapper;

    @Resource
    private BusIntegralLogMapper busIntegralLogMapper;

    @Resource
    private ISmsService smsService;


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;



    @Resource
    private IUserService userService;

    @Override
    public String addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPassword) {
        checkParams(userId,busiPassword,amount);
        BusAccountRecharge busAccountRecharge = new BusAccountRecharge();
        busAccountRecharge.setUserId(userId);
        String orderNo = "SXT_P2P"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ RandomCodesUtils.createRandom(true,10);
        busAccountRecharge.setOrderNo(orderNo);
        busAccountRecharge.setStatus(OrderStatus.PAY_CHECKING.getStatus());
        busAccountRecharge.setRechargeAmount(amount);
        busAccountRecharge.setResource("PC端用户充值");
        busAccountRecharge.setType(RechaggeType.PC.getType());
        busAccountRecharge.setRemark("PC端用户充值");
        busAccountRecharge.setAddtime(new Date());
        AssertUtil.isTrue(busAccountRechargeMapper.insert(busAccountRecharge)<1, XmjfConstant.OPS_FAILED_MSG);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = System.currentTimeMillis()+"";
        //订单名称，必填
        String subject = "用户充值";
        //商品描述，可空
        String body = "用户充值";

        Map<String,Object> params = new HashMap<>();
        params.put("out_trade_no",orderNo);
        params.put("total_amount",amount);
        params.put("subject","用户充值");
        params.put("body","用户充值");
        params.put("product_code","FAST_INSTANT_TRADE_PAY");

        alipayRequest.setBizContent(JSON.toJSONString(params));
        String result=null;
        try {
            //请求
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //输出
        return result;
    }


    private void checkParams(Integer userId, String busiPassword, BigDecimal amount) {
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
        AssertUtil.isTrue(basUserSecurity.getRealnameStatus()!=1,"该用户未实名");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword),"请先输入交易密码");
        AssertUtil.isTrue(!(basUserSecurity.getPaymentPassword().equals(MD5.toMD5(busiPassword))),"交易密码不正确");
        AssertUtil.isTrue(null==amount,"请输入交易金额");
        AssertUtil.isTrue(amount.compareTo(BigDecimal.ZERO)<=0,"支付金额必须大于0元");
    }




    @Override
    public void updateBusAccountRechargeInfo(String orderNo, BigDecimal amount, String sellerid, String appId, String busiNo) {
        /**
         * 1 收集get(同步)|post(异步)参数进行处理
         * 2 检验签名传是否正确,防止中间人攻击
         * 3 校验订单号 app_id sell_id  amount是否合法
         * 4 判断订单是否已支付
         *      已支付-->方法正常结束
         *      未支付-->执行更新操作
         * 5 订单业务更新逻辑处理
         *      更新bus_account_recharge 订单记录表
         *      更新bus_account 账户表
         *      添加账户变动日志信息 bus_account_log
         *      添加用户统计表信息 bus_user_stat
         *      积分表信息更新操作 bus_user_integral
         *      添加用户积分日志记录 bus_integral_log
         *  6 用户充值成功,发送短信通知
         *      userId-->phone
         */
        BusAccountRecharge busAccountRecharge = busAccountRechargeMapper.queryBusAccountRechargeByOrderNo(orderNo);
        AssertUtil.isTrue(StringUtils.isBlank(orderNo)||null==busAccountRecharge,"订单不存在,请联系客服");
        AssertUtil.isTrue(StringUtils.isBlank(busiNo),"订单异常,请联系客服");
        AssertUtil.isTrue(null==amount||amount.compareTo(BigDecimal.ZERO)<=0||amount.compareTo(busAccountRecharge.getRechargeAmount())!=0,"订单金额异常,请联系客服");
        AssertUtil.isTrue(StringUtils.isBlank(sellerid)||StringUtils.isBlank(appId)||!(AlipayConfig.app_id.equals(appId))||!(AlipayConfig.seller_id.equals(sellerid)),"商家信息异常");
        if(busAccountRecharge.getStatus().equals(OrderStatus.PAY_SUCCESS.getStatus())){
            //订单已支付,方法正常执行
            return;
        }
        //设置订单已支付bus_account_recharge
        busAccountRecharge.setStatus(OrderStatus.PAY_SUCCESS.getStatus());
        busAccountRecharge.setActualAmount(amount);
        busAccountRecharge.setAuditTime(new Date());
        busAccountRecharge.setBusiNo(busiNo);
        AssertUtil.isTrue(busAccountRechargeMapper.update(busAccountRecharge)<1,XmjfConstant.OPS_FAILED_MSG);

        //更新bus_account
        BusAccount busAccount = busAccountMapper.queryBusAccountByUserId(busAccountRecharge.getUserId());
        busAccount.setTotal(busAccount.getTotal().add(amount));
        busAccount.setCash(busAccount.getCash().add(amount));
        busAccount.setUsable(busAccount.getUsable().add(amount));
        AssertUtil.isTrue(busAccountMapper.update(busAccount)<1,XmjfConstant.OPS_FAILED_MSG);

        //添加账户变动日志信息 bus_account_log
        BusAccountLog busAccountLog=new BusAccountLog();
        busAccountLog.setUsable(busAccount.getUsable());
        busAccountLog.setUserId(busAccountRecharge.getUserId());
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setOperType("user_recharge");
        busAccountLog.setBudgetType(1);// 收入
        busAccountLog.setRepay(busAccount.getRepay());
        busAccountLog.setRemark("用户充值操作");
        busAccountLog.setOperMoney(amount);
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setAddtime(new Date());
        busAccountLog.setCash(busAccount.getCash());
        busAccountLog.setWait(busAccount.getWait());
        AssertUtil.isTrue(busAccountLogMapper.insert(busAccountLog)<1,XmjfConstant.OPS_FAILED_MSG);


        //添加用户统计表信息 bus_user_stat
        BusUserStat busUserStat=busUserStatMapper.queryBusUserStatByUserId(busAccountRecharge.getUserId());
        busUserStat.setInvestCount(busUserStat.getInvestCount()+1);
        busUserStat.setInvestAmount(busUserStat.getInvestAmount().add(amount));
        AssertUtil.isTrue(busUserStatMapper.update(busUserStat)<1,XmjfConstant.OPS_FAILED_MSG);


        //积分表信息更新操作 bus_user_integral
        BusUserIntegral busUserIntegral=busUserIntegralMapper.queryBusUserIntegralByUserId(busAccountRecharge.getUserId());
        busUserIntegral.setTotal(busUserIntegral.getTotal()+100);
        busUserIntegral.setUsable(busUserIntegral.getUsable()+100);
        AssertUtil.isTrue(busUserIntegralMapper.update(busUserIntegral)<1,XmjfConstant.OPS_FAILED_MSG);


        // 添加用户积分日志记录 bus_integral_log
        BusIntegralLog busIntegralLog=new BusIntegralLog();
        busIntegralLog.setAddtime(new Date());
        busIntegralLog.setIntegral(100);
        busIntegralLog.setStatus(0);
        busIntegralLog.setUserId(busAccountRecharge.getUserId());
        busIntegralLog.setWay("用户充值");
        AssertUtil.isTrue(busIntegralLogMapper.insert(busIntegralLog)<1,XmjfConstant.OPS_FAILED_MSG);

        //用户充值成功,发送短信通知
        BasUser basUser = userService.queryBasUserByUserId(busAccountRecharge.getUserId());
        smsService.sendSms(basUser.getMobile(),XmjfConstant.SMS_RECHARGE_SUCCESS_NOTIFY_TYPE);

    }

    /**
     * 分页信息显示
     * @param busAccountRechargeQuery
     * @return
     */
    @Override
    public PageInfo<Map<String, Object>> queryRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery) {
        /**
         * 加入redis 缓存
         *   缓存添加实现思路
         *     先到redis 查询缓存
         *       存在   获取缓存数据
         *       不存在
         *           查询数据库记录
         *             存在:存储数据到redis 缓存
         */
        Page<Map<String,Object>> vals = null;
        String key = "rechargeList::pageNum::" + busAccountRechargeQuery.getPageNum() + "::pageSize::" + busAccountRechargeQuery.getPageSize() + "::userId::" + busAccountRechargeQuery.getUserId();
        vals = (Page<Map<String, Object>>) valueOperations.get(key);
        if(CollectionUtils.isEmpty(vals)){
            PageHelper.startPage(busAccountRechargeQuery.getPageNum(),busAccountRechargeQuery.getPageSize());
            vals = (Page<Map<String, Object>>)busAccountRechargeMapper.queryRechargesByUserId(busAccountRechargeQuery);
            if(!(CollectionUtils.isEmpty(vals))){
                valueOperations.set(key,vals);
            }
        }
        return new PageInfo<Map<String,Object>>(vals);
    }


}
