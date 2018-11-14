package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.querys.BasItemQuery;

import java.util.Map;

public interface IBasItemService {

    public PageInfo<Map<String,Object>> queryItemsByParams(BasItemQuery basItemQuery);

    public void updateBasItemStatusToOpen(Integer itemId);

    public BasItem queryBasItemByItemId(Integer itemId);
}
