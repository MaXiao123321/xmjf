package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasUser;

public interface BasUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BasUser record);

    int insertSelective(BasUser record);

    BasUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BasUser record);

    int updateByPrimaryKey(BasUser record);
}