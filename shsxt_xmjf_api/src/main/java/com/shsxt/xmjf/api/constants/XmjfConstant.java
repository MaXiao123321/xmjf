package com.shsxt.xmjf.api.constants;

public class XmjfConstant {

    public static final Integer OPS_SUCCESS_CODE=200;
    public static final String  OPS_SUCCESS_MSG="操作成功";

    public static final Integer OPS_FAILED_CODE=300;
    public static final String  OPS_FAILED_MSG="操作失败";

    //  图片验证码常量
    public static final String SESSION_IMAGE="image";

    /////////////////////////////////
    /**
     * 短信类型定义
     */
    public static final int SMS_LOGIN_TYPE=1;
    public static final int SMS_REGISTER_TYPE=2;
    /**
     * 阿里云短信常量配置
     */
    public static final String SMS_AK="LTAITZwzwrJZjJ0L";
    public static final String SMS_SK="qNb1tOZeMgZBxWS7eRcCEFySmNnqme";
    public static final String SMS_PRODUCT="Dysmsapi";
    public static final String SMS_DOMAIN="dysmsapi.aliyuncs.com";
    public static final String SMS_SIGN="上海新房与二手房";
    public static final String SMS_REGISTER_CODE="SMS_150574858";
    public static final String SMS_LOGIN_CODE="SMS_150577168";

}
