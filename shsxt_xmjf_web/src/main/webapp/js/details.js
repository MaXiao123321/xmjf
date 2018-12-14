$(function () {
    $("#itemScale").radialIndicator({
        barColor: 'orange',
        barWidth: 5,
        roundCorner : true,
        radius:35,
        format: '#%'
    });
    var radialObj = $("#itemScale").data('radialIndicator');
    radialObj.value($("#itemScale").attr("data-val"));


    $("#tabs div").click(function () {
        $(this).addClass("tab_active");
        var show = $("#contents .tab_content").eq($(this).index());
        show.show();
        $("#tabs div").not(this).removeClass("tab_active");
        $("#contents .tab_content").not(show).hide();

        if($(this).index()==2){
            //根据当前项目ID查询投资用户记录
            var itemId=$("#itemId").val();
        }


    })




});




function picTab(ele,allNum,currentNum) {
    var ele=$('#imgLarge');
    var allNum=$('#slider').find('li');
    var  currentNum=0;
    allNum.click(function () {
        currentNum = $(this).index();
        ele.show(300);
        var ImgSrc = $(this).attr('data-url');

        ele.css('background-image', 'url('+ImgSrc+')');
    });
    $('.close').click(function () {
        ele.hide(300);
    });
    $('.left').click(function () {
        currentNum--;
        if (currentNum < 0) {
            currentNum = allNum.length - 1;
        }
        var ImgSrc = allNum.eq(currentNum).attr('data-url');
        ele.css('background-image', 'url('+ImgSrc+')');
    });

    $('.right').click(function () {
        currentNum++;
        if (currentNum > allNum.length - 1) {
            currentNum = 0;
        }
        var ImgSrc = allNum.eq(currentNum).attr('data-url');
        ele.css('background-image', 'url('+ImgSrc+')');
    })
}

function toRecharge() {
    $.ajax({
        url:ctx+"/user/checkRealNameStatus",
        type:"get",
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                window.location.href=ctx+"/account/recharge";
            }else{
                layer.confirm('当前用户未实名,执行实名认证?', {
                    btn: ['实名认证','稍后认证'] //按钮
                }, function(){
                    window.location.href=ctx+"/auth";
                });
            }
        }
    })
}


function doInvest() {
    var itemId=$("#itemId").val();
    var usable=parseFloat($("#usable").attr("data-value"))//账户可用金额
    var amount=$("#usableMoney").val();
    var syAmount=parseFloat($("#syAmount").attr("data-value"));
    if(isEmpty(amount)){
        layer.tips("请输入投资金额!","#usableMoney");
        return;
    }
    amount=parseFloat(amount);
    if(amount>usable){
        layer.tips("投资金额不能大于账户可用金额!","#usableMoney");
        return;
    }
    var singleMinInvestAmount=$("#minInvestMoney").attr("data-value");
    if(!isEmpty(singleMinInvestAmount)&&syAmount<singleMinInvestAmount){
        layer.tips("项目即将截标,不能进行投资操作!","#usableMoney");
        return;
    }
    if(!isEmpty(singleMinInvestAmount)&&amount<singleMinInvestAmount){
        layer.tips("投资金额不能小于单笔最小投资!","#usableMoney");
        return;
    }
    var singleMaxInvestAmount=$("#maxInvestMoney").attr("data-value");
    if(!isEmpty(singleMaxInvestAmount)&&amount>singleMaxInvestAmount){
        layer.tips("投资金额不能大于单笔最大投资!","#usableMoney");
        return;
    }
    if(amount>syAmount){
        layer.tips("投资金额不能大于项目剩余资金!","#usableMoney");
        return;
    }

    layer.prompt({title: '随便写点啥，并确认', formType: 1}, function(text, index) {
        layer.close(index);
        var busiPassword=text;
        /**
         * 发送ajax
         */
        $("#doInvest").attr("disabled",true);
        $("#doInvest").css("background","#7c7c7c");
        $.ajax({
            type:"post",
            url:ctx+"/invest/doInvest",
            data:{
                itemId:itemId,
                amount:amount,
                busiPassword:busiPassword
            },
            dataType:"json",
            success:function (data) {
                $("#doInvest").attr("disabled",false);
                $("#doInvest").css("background","#fcb22f");
                window.location.href=ctx+"/account/index";
            }
        })
    })

}
