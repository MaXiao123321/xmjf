package com.shsxt.xmjf.api.service;


import com.shsxt.xmjf.api.po.User;

public interface IUserService {
    public User queryUserByUserId(Integer userId);
}
