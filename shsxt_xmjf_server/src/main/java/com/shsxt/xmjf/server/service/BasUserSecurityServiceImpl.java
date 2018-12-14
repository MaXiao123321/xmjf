package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.server.db.dao.BasUserSecurityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BasUserSecurityServiceImpl implements IBasUserSecurityService {

    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;
    @Override
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId) {
        return basUserSecurityMapper.queryBasUserSecurityByUserId(userId);
    }

    @Override
    public int updateBasUserSecurity(BasUserSecurity basUserSecurity) {
        return basUserSecurityMapper.update(basUserSecurity);
    }

}
