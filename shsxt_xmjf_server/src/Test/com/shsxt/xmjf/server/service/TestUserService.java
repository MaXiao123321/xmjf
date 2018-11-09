package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestUserService {

    @Resource
    private IUserService userService;

    @Test
    public void Test01(){
        userService.saveUser("17521518913","123456","243563");
    }

}
