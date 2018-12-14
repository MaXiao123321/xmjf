package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BusAccount;

import java.util.Map;

public interface IAccountService {

    public BusAccount queryBusAccountByUserId(Integer userId);

    public Map<String,Object> countBusAccountInfoByUserId(Integer userId);

}
