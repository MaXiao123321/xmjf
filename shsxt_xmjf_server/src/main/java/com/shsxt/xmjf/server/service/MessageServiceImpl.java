package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.service.ISmsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class MessageServiceImpl {

    @Resource
    private ISmsService smsService;

    public void  doSendSms(Map<String,Object> map){
        String phone = (String) map.get("phone");
        Integer type = (Integer) map.get("type");
        smsService.sendSms(phone,type);
    }

}
