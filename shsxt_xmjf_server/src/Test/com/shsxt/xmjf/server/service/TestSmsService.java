package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import org.apache.ibatis.javassist.ClassPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestSmsService {

    @Resource
    private ISmsService smsService;

   @Test
    public void Test01(){
        smsService.sendSms("13816298614", XmjfConstant.SMS_REGISTER_TYPE);
    }

}
