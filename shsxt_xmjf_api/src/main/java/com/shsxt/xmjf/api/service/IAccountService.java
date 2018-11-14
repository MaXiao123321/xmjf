package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BusAccount;

public interface IAccountService {

    public BusAccount queryBusAccountByUserId(Integer userId);

}
