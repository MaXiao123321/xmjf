$(function () {
    $(".validImg").click(function () {
        $(this).attr("src", ctx + "/image");
    });



    /*$("#clickMes").click(function () {
        var phone=$("#phone").val();
        var code=$("input[name='code']").val();
        if(null==phone||""==phone||""==phone.trim()||phone=="undefined"){
            layer.tips("请输入手机号!","#phone");
            return;
        }
        if(isEmpty(code)){
            layer.tips("请输入图片验证码!","input[name='code']");
            return;
        }
        console.log(phone+","+code);


        // 按钮禁用


        $.ajax({
            type:"post",
            url:ctx+"/sms",
            data:{
                phone:phone,
                imageCode:code,
                type:2
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    /!**
                     * 倒计时 60秒不能重复点击
                     *!/
                    djs();
                }else{
                    layer.tips(data.msg,"#clickMes");
                }
            }
        })

    });*/

    var handler2 = function (captchaObj) {
        $("#clickMes").click(function (e) {
            var phone = $("#phone").val();
            var result = captchaObj.getValidate();
            if (!result) {
                layer.tips("请先完成验证","#captcha2");
            } else {
                if(isEmpty(phone)){
                    layer.tips("请输入手机号!","#phone");
                    return;
                }
                $.ajax({
                    url: 'gt/ajax-validate2',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        phone:phone,
                        type:2,
                        geetest_challenge: result.geetest_challenge,
                        geetest_validate: result.geetest_validate,
                        geetest_seccode: result.geetest_seccode
                    },
                    success: function (data) {
                        if(data.code==200){
                            /**
                             * 倒计时 60秒不能重复点击
                             */

                            djs();
                        }else{
                            layer.tips(data.msg,"#clickMes");
                        }
                    }
                })
            }
            e.preventDefault();
        });
        // 将验证码加到id为captcha的元素里，同时会有三个input的值用于表单提交
        captchaObj.appendTo("#captcha2");
        captchaObj.onReady(function () {
            $("#wait2").hide();
        });
        // 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
    };
    $.ajax({
        url: "gt/register2", // 加随机数防止缓存
        type: "get",
        dataType: "json",
        success: function (data) {
            // 调用 initGeetest 初始化参数
            // 参数1：配置参数
            // 参数2：回调，回调的第一个参数验证码对象，之后可以使用它调用相应的接口
            initGeetest({
                gt: data.gt,
                challenge: data.challenge,
                new_captcha: data.new_captcha, // 用于宕机时表示是新验证码的宕机
                offline: !data.success, // 表示用户后台检测极验服务器是否宕机，一般不需要关注
                product: "popup", // 产品形式，包括：float，popup
                width: "100%"
                // 更多配置参数请参见：http://www.geetest.com/install/sections/idx-client-sdk.html#config
            }, handler2);
        }
    });
    
    $("#register").click(function () {
        var phone=$("#phone").val();
        var password=$("#password").val();
        var code=$("#verification").val();
        if(isEmpty(phone)){
            layer.tips("请输入手机号!","#phone");
            return;
        }
        if(isEmpty(code)){
            layer.tips("请输入手机验证码!","#verification");
            return;
        }

        if(isEmpty(password)){
            layer.tips("请输入密码!","#password");
            return;
        }

        $.ajax({
            type:"post",
            url:ctx+"/user/register",
            data:{
                phone:phone,
                password:password,
                code:code
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    window.location.href=ctx+"/login";
                }else{
                    layer.tips(data.msg,"#register");
                }
            }
        })





    })


});


function  djs() {
    var time=60;
    var intervalId= setInterval(function () {
        if(time>=1){
            $("#clickMes").attr("disabled",true);
            $("#clickMes").val("("+time+"秒)");
            $("#clickMes").css("background","#7c7c7c");
            time--;
        }else{
            $("#clickMes").attr("disabled",false);
            $("#clickMes").val("获取验证码");
            $("#clickMes").css("background","#fcb22f");
            clearInterval(intervalId);// 清除定时器
        }
    },1000)
}
