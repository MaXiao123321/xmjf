package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusItemInvest;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;
import com.shsxt.xmjf.server.base.BaseMapper;

import java.util.List;
import java.util.Map;

public interface BusItemInvestMapper extends BaseMapper<BusItemInvest> {
    public List<Map<String,Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery);

    /**
     * 统计用户投资的新手标项目
     */
    public int countUserInvestNewItemByUserId(Integer userId);

    public List<Map<String,Object>> countInvestIncomeInfoByUserId(Integer userId);
}