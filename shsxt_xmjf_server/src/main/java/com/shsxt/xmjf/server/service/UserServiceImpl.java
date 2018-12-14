package com.shsxt.xmjf.server.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.*;
import com.shsxt.xmjf.server.base.BaseMapper;
import com.shsxt.xmjf.server.db.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserDao userDao;

    @Resource
    private BasUserMapper basUserMapper;

    @Resource
    private BasUserInfoMapper basUserInfoMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;

    @Resource
    private BasExperiencedGoldMapper basExperiencedGoldMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource(name = "redisTemplate")
    private ValueOperations valueOperations;

    @Resource
    private BusAccountMapper busAccountMapper;


    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private AmqpTemplate amqpTemplate;

    /*@Override
    public User queryUserByUserId(Integer userId) {
        return userDao.queryUserByUserId(userId);
    }

    @Override
    public BasUser queryBasUserByPhone(String phone) {
        return basUserMapper.queryBasUserByPhone(phone);
    }*/


    @Override
    public User queryUserByUserId(Integer userId) {
       /*
         * 加入redis 缓存
         *   缓存添加实现思路
         *     先到redis 查询缓存
         *       存在   获取缓存数据
         *       不存在
         *           查询数据库记录
         *             存在:存储数据到redis 缓存*/

        String key = "user::userId::"+userId;
        User user = (User) valueOperations.get(key);
        if(null==user){
            user = userDao.queryUserByUserId(userId);
            if(user!=null){
                valueOperations.set(key,user);
            }
        }
        return user;
    }
    @Override
    public BasUser queryBasUserByPhone(String phone) {
        /*
         * 加入redis 缓存
         *   缓存添加实现思路
         *     先到redis 查询缓存
         *       存在   获取缓存数据
         *       不存在
         *           查询数据库记录
         *             存在:存储数据到redis 缓存
         */

        String key="user::phone::"+phone;
        BasUser basUser= (BasUser) valueOperations.get(key);
        if(null==basUser){
            basUser = basUserMapper.queryBasUserByPhone(phone);
            if(null!=basUser){
                valueOperations.set(key,basUser);
            }
        }
        return basUser;
    }

    /**
     * 用户注册
     * @param phone 用户电话
     * @param password 用户注册密码
     * @param code 用户注册的验证码
     */
    @Override
    public void saveUser(String phone, String password, String code) {
        /**
         * 1 参数校验
         *      phone 非空,唯一
         *      密码 非空,长度至少六位
         *      code 有效,值与缓存值相等
         * 2 表数据初始化
         *       bas_user	用户基本信息
                 bas_user_info	用户信息扩展表记录添加


                 bas_user_security	用户安全信息表
                 bus_account	用户账户表记录信息
                 bus_user_integral	用户积分记录
                 bus_income_stat	用户收益表记录
                 bus_user_stat	用户统计表



                 bas_experienced_gold	注册体验金表
                 sys_log     系统日志
            3 短信通知
         */
        //1 参数校验
        checkParams(phone,password,code);

        //2 表数据初始化
        //2-1 bas_user表初始化数据信息
        int userId = initBasUser(phone,password);

        //2-2 bas_user_info 用户信息扩展表记录添加
        initBasUserInfo(userId,phone);

        //2-3 bas_user_security	用户安全信息表
        initBasUserSecurity(userId);

        //2-4 bus_account	用户账户表记录信息
        initBusAccount(userId);

        //2-5 bus_user_integral	用户积分记录
        //initBusUserIntegral(userId);

        //2-7 bas_experienced_gold	注册体验金表
        initBasExperiencedGold(userId);

        //2-8 sys_log     系统日志
        initSysLog(userId);

        //发送短信
        //异步消息,短信异步发送
        Map<String,Object> map=new HashMap<>();
        map.put("phone",phone);
        map.put("type",XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE);
        amqpTemplate.convertAndSend(map);


    }

    /**
     * 登录操作
     * @param phone 手机号
     * @param password 密码
     * @return
     */
    @Override
    public UserModel login(String phone, String password) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入手机号");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)),"手机号不合法");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码不能为空");
        BasUser basUser = queryBasUserByPhone(phone);
        AssertUtil.isTrue(null==basUser,"该手机号未注册,请先执行注册!");
        AssertUtil.isTrue(!(basUser.getPassword().equals(MD5.toMD5(password+basUser.getSalt()))),"密码不正确");
        UserModel userModel=new UserModel();
        userModel.setUserName(basUser.getUsername());
        userModel.setUserId(basUser.getId());
        userModel.setMobile(basUser.getMobile());
        return userModel;
    }

    @Override
    public UserModel quickLogin(String phone, String code) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入手机号");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)),"手机号不合法");
        AssertUtil.isTrue(StringUtils.isBlank(code),"验证码不能为空");
        String key="phone::"+phone+"::type::"+ XmjfConstant.SMS_LOGIN_TYPE;
        AssertUtil.isTrue(!(redisTemplate.hasKey(key)),"验证码不存在或已过期!");
        BasUser basUser =queryBasUserByPhone(phone);
        AssertUtil.isTrue(null==basUser,"该手机号未注册,请先执行注册!");
        UserModel userModel=new UserModel();
        userModel.setUserName(basUser.getUsername());
        userModel.setUserId(basUser.getId());
        userModel.setMobile(basUser.getMobile());

        return userModel;
    }


    private void initSysLog(int userId) {
        SysLog sysLog = new SysLog();
        sysLog.setUserId(userId);
        sysLog.setAddtime(new Date());
        //做什么操作
        sysLog.setCode("REGISTER");
        sysLog.setOperating("用户注册");
        sysLog.setResult(1);
        sysLog.setType(4);// 注册
        AssertUtil.isTrue(sysLogMapper.insert(sysLog)<1,XmjfConstant.OPS_FAILED_MSG);
    }

    private void initBasExperiencedGold(int userId) {
        BasExperiencedGold basExperiencedGold = new BasExperiencedGold();
        basExperiencedGold.setUserId(userId);
        basExperiencedGold.setWay("注册获取");
        basExperiencedGold.setUsefulLife(15);//15天有效
        basExperiencedGold.setStatus(2);
        basExperiencedGold.setGoldName("注册体验金");
        //设置过期时间
        basExperiencedGold.setExpiredTime(DateUtil.offsetDay(new Date(),15));
        basExperiencedGold.setAmount(BigDecimal.valueOf(2888));
        basExperiencedGold.setAddtime(new Date());
        AssertUtil.isTrue(basExperiencedGoldMapper.insert(basExperiencedGold)<1,XmjfConstant.OPS_FAILED_MSG);
    }

    private void initBusAccount(int userId) {
        BusAccount busAccount = new BusAccount();
        busAccount.setUserId(userId);
        //数据库中数据全部不能为空
        busAccount.setCash(BigDecimal.valueOf(0));
        busAccount.setFrozen(BigDecimal.valueOf(0));
        busAccount.setRepay(BigDecimal.valueOf(0));
        busAccount.setRepay(BigDecimal.valueOf(0));
        busAccount.setTotal(BigDecimal.valueOf(0));
        busAccount.setUsable(BigDecimal.valueOf(0));
        busAccount.setWait(BigDecimal.valueOf(0));
        AssertUtil.isTrue(busAccountMapper.insert(busAccount)<1,XmjfConstant.OPS_FAILED_MSG);
    }

    private void initBasUserSecurity(int userId) {
        BasUserSecurity basUserSecurity = new BasUserSecurity();
        basUserSecurity.setUserId(userId);
        AssertUtil.isTrue(basUserSecurityMapper.insert(basUserSecurity)<1,XmjfConstant.OPS_FAILED_MSG);
    }

    private void initBasUserInfo(int userId, String phone) {
        BasUserInfo basUserInfo = new BasUserInfo();
        basUserInfo.setUserId(userId);
        basUserInfo.setCustomerType(0);
        //唯一邀请码
        basUserInfo.setInviteCode(phone);
        AssertUtil.isTrue(basUserInfoMapper.insert(basUserInfo)<1,XmjfConstant.OPS_FAILED_MSG);
    }

    private int initBasUser(String phone, String password) {
        BasUser basUser = new BasUser();
        basUser.setUsername(phone);
        basUser.setMobile(phone);
        String salt = RandomCodesUtils.createRandom(false,6);
        basUser.setSalt(salt);
        basUser.setPassword(MD5.toMD5(password+salt));
        basUser.setReferer("PC");
        basUser.setTime(new Date());
        basUser.setAddtime(new Date());
        basUser.setType(1);
        basUser.setStatus(1);
        AssertUtil.isTrue(basUserMapper.insert(basUser)<1,XmjfConstant.OPS_FAILED_MSG);
        return basUser.getId();
    }

    /**
     * 验证参数
     * @param phone
     * @param password
     * @param code
     */
    private void checkParams(String phone, String password, String code) {
        //验证手机号是否为空
        AssertUtil.isTrue(StringUtils.isBlank(phone), "请输入手机号");
        //验证手机号是否存在
        AssertUtil.isTrue(!PhoneUtil.checkPhone(phone), "手机号不合法");
        //手机号必须唯一
        AssertUtil.isTrue(null!=queryBasUserByPhone(phone),"该手机号已注册");
        //验证密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(password), "请输入密码");
        //验证密码是否符合规范
        AssertUtil.isTrue(password.length()<6,"密码至少六位");
        //短信验证码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(code),"短信验证码不能为空");
        //验证短信验证码是否存在或已过期
        String key="phone::"+phone+"::type::"+ XmjfConstant.SMS_REGISTER_TYPE;
        AssertUtil.isTrue(!(redisTemplate.hasKey(key)),"验证码不存在或已过期!");
        AssertUtil.isTrue(!(redisTemplate.opsForValue().get(key).toString().equals(code)),"验证码不正确");
    }


    //用户实名认证
    @Override
    public ResultInfo updateBasUserSecurityInfo(String realName, String cardNo, Integer userId, String busiPassword) {
        //调用阿里云认证接口
        ResultInfo resultInfo = new ResultInfo();
        try {
            //验证参数
            checkParams02(realName, cardNo,busiPassword);
            BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
            AssertUtil.isTrue(basUserSecurity.getRealnameStatus()==1,"当前用户已实名");
            doAuth(realName, cardNo);
            basUserSecurity.setIdentifyCard(cardNo);
            basUserSecurity.setRealname(realName);
            basUserSecurity.setRealnameStatus(1);
            basUserSecurity.setVerifyTime(new Date());
            basUserSecurity.setPaymentPassword(MD5.toMD5(busiPassword));
            AssertUtil.isTrue(basUserSecurityService.updateBasUserSecurity(basUserSecurity)<1,XmjfConstant.OPS_FAILED_MSG);
        }catch (BusiException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        }catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("认证未通过!");
        }

        return resultInfo;
    }

    @Override
    public ResultInfo checkRealNameStatus(Integer userId) {
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
        ResultInfo resultInfo = new ResultInfo("已实名");
        if(basUserSecurity.getRealnameStatus()!=1){
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("用户未实名");
        }
        return resultInfo;
    }

    @Override
    public BasUser queryBasUserByUserId(Integer userId) {
        return basUserMapper.queryById(userId);
    }

    /**
     * 调用阿里云的接口
     * @param realName 客户的真是姓名
     * @param cardNo 客户的身份证号码
     */
    private void doAuth(String realName, String cardNo) throws  Exception{
            String host = XmjfConstant.SM_HOST;
            String path = XmjfConstant.SM_PATH;
            String method = XmjfConstant.SM_REQUEST_TYPE;
            String appcode = XmjfConstant.SM_CODE;
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            Map<String, String> bodys = new HashMap<String, String>();
            bodys.put("cardNo", cardNo);
            bodys.put("realName", realName);


            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());

            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            System.out.println(response.getEntity());
            AssertUtil.isTrue(jsonObject.getInteger("error_code")!=0,jsonObject.getString("reason"));

    }

    private void checkParams02(String realName, String cardNo, String busiPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(realName), "请输入真实姓名!");
        AssertUtil.isTrue(StringUtils.isBlank(cardNo), "请输入身份证号!");
        AssertUtil.isTrue(cardNo.length() != 18, "身份证号长度不合法!");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword), "请输入交易密码!");
    }

    public static void main(String[] args) {
        /*String host = "https://1.api.apistore.cn";
        String path = "/idcard3";
        String method = "POST";
        String appcode = "a386f386635c4dc89638459e09469710";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("cardNo", "123");
        bodys.put("realName", "张三");
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());

            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String result = "{\"error_code\":80004,\"reason\":\"身份证号码格式不正确\",\"result\":{\"realName\":\"张三\",\"cardNo\":\"123\"},\"ordersign\":\"20181114171159073021025019\"}";
        Object object= JSON.parse(result);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject.getInteger("error_code") + "--" + jsonObject.getString("reason"));

    }

}
