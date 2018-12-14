package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;

import java.math.BigDecimal;
import java.util.Map;

public interface IBusAccountRechargeService {

    public String addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPassword);

    public void updateBusAccountRechargeInfo(String orderNo,BigDecimal amount,String sellerid,String appId,String busiNo);

    public PageInfo<Map<String,Object>> queryRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery);

}
