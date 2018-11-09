package com.shsxt.xmjf.api.exceptions;

import com.shsxt.xmjf.api.constants.XmjfConstant;

public class BusiException extends RuntimeException {

    private Integer code= XmjfConstant.OPS_FAILED_CODE;
    private String msg = XmjfConstant.OPS_FAILED_MSG;

    public BusiException() {
        super(XmjfConstant.OPS_FAILED_MSG);
    }

    public BusiException(Integer code) {
        super(XmjfConstant.OPS_FAILED_MSG);
        this.code = code;
    }

    public BusiException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusiException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BusiException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

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
