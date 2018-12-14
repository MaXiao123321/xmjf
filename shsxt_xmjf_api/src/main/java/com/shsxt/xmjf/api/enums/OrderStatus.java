package com.shsxt.xmjf.api.enums;

public enum OrderStatus {

    PAY_FILLED(0),//支付失败
    PAY_SUCCESS(1),//支付成功
    PAY_CHECKING(2);//审核中
    private Integer status;

    OrderStatus(Integer status){
        this.status=status;
    }

    public Integer getStatus() {
        return status;
    }
}
