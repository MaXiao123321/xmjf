package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;

import java.math.BigDecimal;
import java.util.Map;

public interface IBusItemInvestService {

    public PageInfo<Map<String,Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery);

    /**
     * 用户投资方法声明
     * @param itemId
     * @param amount
     * @param userId
     * @param busiPassword
     */
    public void addBusItemInvest(Integer itemId, BigDecimal amount,Integer userId,String busiPassword);


    public Map<String,Object> countInvestIncomeInfoByUserId(Integer userId);

}
