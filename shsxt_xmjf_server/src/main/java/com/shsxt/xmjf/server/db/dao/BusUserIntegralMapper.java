package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusUserIntegral;

public interface BusUserIntegralMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusUserIntegral record);

    int insertSelective(BusUserIntegral record);

    BusUserIntegral selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusUserIntegral record);

    int updateByPrimaryKey(BusUserIntegral record);
}