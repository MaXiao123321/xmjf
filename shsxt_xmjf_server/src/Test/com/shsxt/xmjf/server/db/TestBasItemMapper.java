package com.shsxt.xmjf.server.db;


import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.server.db.dao.BasItemMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestBasItemMapper {

    @Resource
    private BasItemMapper basItemMapper;

    @Test
    public void Test(){
        BasItemQuery basItemQuery = new BasItemQuery();
        basItemQuery.setIsHistory(1);
        basItemQuery.setItemCycle(2);
        basItemQuery.setItemType(3);
        List<Map<String, Object>> vals = basItemMapper.queryItemsByParams(basItemQuery);
        if(!(CollectionUtils.isEmpty(vals))){
            for(Map<String,Object> map:vals){
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    System.out.println(entry.getKey()+"---"+entry.getValue());
                }
                System.out.println("--------------------------------");
            }
        }
    }

}
