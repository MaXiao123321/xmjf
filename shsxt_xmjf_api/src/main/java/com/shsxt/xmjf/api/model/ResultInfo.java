package com.shsxt.xmjf.api.model;

import java.io.Serializable;

public class ResultInfo implements Serializable{
    private static final long serialVersionUID = 1679258444606548109L;
    private Integer code=200;
    private String msg="操作成功";

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
