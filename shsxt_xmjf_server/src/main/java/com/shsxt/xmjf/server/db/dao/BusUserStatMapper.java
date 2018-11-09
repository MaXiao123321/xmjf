package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusUserStat;
import com.shsxt.xmjf.api.po.BusUserStatKey;

public interface BusUserStatMapper {
    int deleteByPrimaryKey(BusUserStatKey key);

    int insert(BusUserStat record);

    int insertSelective(BusUserStat record);

    BusUserStat selectByPrimaryKey(BusUserStatKey key);

    int updateByPrimaryKeySelective(BusUserStat record);

    int updateByPrimaryKey(BusUserStat record);
}