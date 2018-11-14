package com.shsxt.xmjf.api.constants;

public class XmjfConstant {

    public static final Integer OPS_SUCCESS_CODE=200;
    public static final String  OPS_SUCCESS_MSG="操作成功";

    public static final Integer OPS_FAILED_CODE=300;
    public static final String  OPS_FAILED_MSG="操作失败";

    public static final Integer NO_LOGIN_CODE=305;
    public static final String  NO_LOGIN_MSG="未登录";

    //  图片验证码常量
    public static final String SESSION_IMAGE="image";
    // 用户session信息常量
    public static final String SESSION_USER="userInfo";


    public static final int PAGE_NUM=1;
    public static final int PAGE_SIZE=10;

    /////////////////////////////////
    /**
     * 短信类型定义
     */
    public static final int SMS_LOGIN_TYPE=1;
    public static final int SMS_REGISTER_TYPE=2;
    public static final int SMS_REGISTER_SUCCESS_NOTIFY_TYPE=3;
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
    public static final String SMS_REGISTER_SUCCESS_NOTIFY_CODE="SMS_150579866";
}
