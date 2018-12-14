package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.service.IAccountService;
import com.shsxt.xmjf.server.db.dao.BusAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusAccountServiceImpl implements IAccountService {

    @Resource
    private BusAccountMapper busAccountMapper;

    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
        return busAccountMapper.queryBusAccountByUserId(userId);
    }

    @Override
    public Map<String, Object> countBusAccountInfoByUserId(Integer userId) {
        Map<String,Object> result=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String, Object> accountMap = busAccountMapper.countBusAccountInfoByUserId(userId);
        for(Map.Entry<String,Object> entry:accountMap.entrySet()){
            if(!(entry.getKey().equals("总资产"))){
                Map<String,Object> temp=new HashMap<String,Object>();
                temp.put("name",entry.getKey());
                temp.put("y",entry.getValue());
                list.add(temp);
            }else{
                result.put("data1",entry.getValue());
            }
        }
        result.put("data2",list);
        return result;
    }
}
