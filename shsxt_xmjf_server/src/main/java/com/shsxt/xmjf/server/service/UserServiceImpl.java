package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.server.db.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserDao userDao;

    @Override
    public User queryUserByUserId(Integer userId) {
        return userDao.queryUserByUserId(userId);
    }
}
