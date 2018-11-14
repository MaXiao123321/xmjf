package com.shsxt.xmjf.server.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.ItemStatus;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.server.db.dao.BasItemMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class BasItemServiceImpl implements IBasItemService {

    @Resource
    private BasItemMapper basItemMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;

    @Override
    public PageInfo<Map<String, Object>> queryItemsByParams(BasItemQuery basItemQuery) {
        /**
         * 加入redis 缓存
         *   缓存添加实现思路
         *     先到redis 查询缓存
         *       存在   获取缓存数据
         *       不存在
         *           查询数据库记录
         *             存在:存储数据到redis 缓存
         */
        Page<Map<String,Object>> vals = null;
        String key = "itemList::pageNum::" + basItemQuery.getPageNum() + "::pageSize::" + basItemQuery.getPageSize() +
                "::itemCycle::" + basItemQuery.getItemCycle() + "::itemType::" + basItemQuery.getItemType() +
                "::isHistory::" + basItemQuery.getIsHistory();
        vals = (Page<Map<String,Object>>)valueOperations.get(key);
        if(null==vals){
            PageHelper.startPage(basItemQuery.getPageNum(),basItemQuery.getPageSize());
            vals = (Page<Map<String, Object>>) basItemMapper.queryItemsByParams(basItemQuery);
            if(!CollectionUtils.isEmpty(vals)){
                for(Map<String,Object> map : vals){
                    int item_status= (int) map.get("item_status");
                    if(item_status== ItemStatus.WAITOPEN){
                        Date releaseTime= (Date) map.get("release_time");
                        Long syTime= (releaseTime.getTime()-System.currentTimeMillis())/1000;
                        map.put("syTime",syTime);
                    }
                    if(item_status==ItemStatus.OPEN){
                        BigDecimal itemAccount= (BigDecimal) map.get("item_account");
                        BigDecimal itemOnGoingAccount= (BigDecimal) map.get("item_ongoing_account");
                        map.put("syAccount",itemAccount.subtract(itemOnGoingAccount));
                    }
                }
                valueOperations.set(key,vals);
            }
        }
        return new PageInfo<Map<String, Object>>(vals);
    }

    @Override
    public BasItem queryBasItemByItemId(Integer itemId){
        String key = "basItem::itemId::"+itemId;
        BasItem basItem = (BasItem) valueOperations.get(key);
        if(null==basItem){
            basItem = basItemMapper.queryById(itemId);
            if(basItem!=null){
                valueOperations.set(key,basItem);
            }
        }
        return basItem;
    }


    @Override
    public void updateBasItemStatusToOpen(Integer itemId) {
        BasItem basItem = queryBasItemByItemId(itemId);
        AssertUtil.isTrue(null==basItem||null==itemId,"记录不存在");
        basItem.setItemStatus(ItemStatus.OPEN);
        AssertUtil.isTrue(basItemMapper.update(basItem)<1, XmjfConstant.OPS_FAILED_MSG);

        //模糊查找itemList* key 清楚缓存操作
        Set<String> keys=redisTemplate.keys("itemList*");
        redisTemplate.delete(keys);
    }
}
