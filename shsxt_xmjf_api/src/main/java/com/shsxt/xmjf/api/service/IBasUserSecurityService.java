package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BasUserSecurity;

public interface IBasUserSecurityService {

    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId);

    public int updateBasUserSecurity(BasUserSecurity basUserSecurity);

}
