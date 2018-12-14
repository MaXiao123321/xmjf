package com.shsxt.xmjf.api.querys;

import com.shsxt.xmjf.api.constants.XmjfConstant;

import java.io.Serializable;

public class ItemInvestQuery implements Serializable {

    private static final long serialVersionUID = 8526535113332857158L;
    private Integer itemId;
    private Integer pageNum= XmjfConstant.PAGE_NUM;
    private Integer pageSize=XmjfConstant.PAGE_SIZE;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
