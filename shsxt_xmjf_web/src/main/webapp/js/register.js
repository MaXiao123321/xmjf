$(function () {
    $('.validImg').click(function () {
        $(this).attr("src",ctx+"/image");
    });

    $('#clickMes').click(function () {
        var phone = $('#phone').val();
        var code = $('input[name="code"]').val();
        if(phone==null||""==phone||""==phone.trim()||phone=="undefined"){
            layer.tips('请输入手机号', '#phone');
            return;
        }
        if(isEmpty(code)){
            layer.tips("请输入图片验证码!","input[name='code']");
            return;
        }
        console.log(phone+","+code);

        //按钮禁用
        $.ajax({
            type:"post",
            url:ctx+'/sms',
            data:{
                phone:phone,
                imageCode:code,
                type:2
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    /**
                     * 倒计时60秒不能重复点击
                     */
                    djs();
                }else{
                    layer.tips(data.msg,"#clickMes");
                }

            }
        })

    });

    $('#register').click(function () {
        var phone = $('#phone').val();
        var password = $('#password').val();
        var code = $('#verification').val();
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
            url:ctx+'/user/register',
            type:'post',
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
                    layer.tips(data.msg,'#register');
                }
            }
        })
    })

});

function djs() {
    var time=6;
    var intervalId = setInterval(function () {
        if(time>0){
            $('#clickMes').attr("disabled",true);
            $('#clickMes').val("("+time+")秒");
            $('#clickMes').css("background","#7c7c7c");
            time--;
        }else{
            $("#clickMes").attr("disabled",false);
            $("#clickMes").val("获取验证码");
            $("#clickMes").css("background","#fcb22f");
            clearInterval(intervalId);//清楚定时器
        }
    },1000)
}