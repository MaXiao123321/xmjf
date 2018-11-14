$(function () {
   //加载投资数据列表
    loadInvestListData();

    $(".tab").click(function () {
        $(this).addClass("list_active");
        $(".tab").not(this).removeClass("list_active");
        var itemCycle=$(this).index();
        var isHistory=0;
        if(itemCycle==4){
            isHistory=1;
        }
        var itemType=$("#itemType").val();
        loadInvestListData(itemCycle,itemType,isHistory);
    })
    
});

/**
 * 加载投资列表数据
 */
function loadInvestListData(itemCycle,itemType,isHistory,pageNum,pageSize) {
    var params = {
      isHistory:0,
      pageNum:1,
      pageSize:10
    };

    if(!isEmpty(itemCycle)){
        params.itemCycle=itemCycle;
    }
    if(!isEmpty(itemType)){
        params.itemType=itemType;
    }
    if(!isEmpty(isHistory) && isHistory!=0){
        params.isHistory=1;
    }
    if(!isEmpty(pageNum) && pageNum>1){
        params.pageNum=pageNum;
    }
    if(!isEmpty(pageSize) && pageSize!=10){
        params.pageSize=pageSize;
    }


    $.ajax({
        type:"post",
        url:ctx+"/item/list",
        data:params,
        dataType:"json",
        success:function (data) {
            var list=data.list;
            var pages=data.navigatepageNums;
            if(data.total>0){
                /**
                 * 初始化list列表数据
                 * 初始化页面数据
                 */
                initTrHtml(list);
                initPageHtml(pages,data.pageNum);
                //初始化项目进度
                initItemScale();
                // 初始化项目剩余时间
                initItemSyTime();
            }else{
                alert("暂无数据");
                $("#pcItemList").html("");
                $("#pages").html("")
            }
        }
    })
}

function initTrHtml(list) {
    if(list.length>0){
        var trs="";
        for(var i=0;i<list.length;i++){
            var temp = list[i];
            trs=trs+"<tr>";
            /**
             * 拼接单元格(项目利率)
             */

            trs=trs+"<td>";
            trs=trs+"<strong>"+temp.item_rate+"</strong>%";
            if(temp.item_add_rate>0){
                trs=trs+"<span>+"+temp.item_add_rate+"%</span>";
            }
            trs=trs+"</td>";
            /**
             * 项目期限
             */
            trs=trs+"<td>";
            if(temp.item_cycle_unit==1){
                trs=trs+temp.item_cycle+"|天";
            }
            if(temp.item_cycle_unit==2){
                trs=trs+temp.item_cycle+"|月";
            }
            if(temp.item_cycle_unit==3){
                trs=trs+temp.item_cycle+"|季";
            }
            if(temp.item_cycle_unit==4){
                trs=trs+temp.item_cycle+"|月";
            }
            trs=trs+"</td>";

            /**
             * 项目名称
             */
            trs=trs+"<td>";
            trs=trs+temp.item_name;
            if(temp.item_isnew==1){
                trs=trs+"<strong class='colorful' new>NEW</strong>";
            }
            if(temp.item_isnew==1 && temp.move_vip==1){
                trs=trs+"<strong class='colorful' app>APP</strong>";
            }
            if(temp.item_isnew==1 && temp.move_vip==0 && temp.item_isrecommend ==1){
                trs=trs+"<strong class='colorful' app>HOT</strong>";
            }
            if(temp.item_isnew==1 && temp.move_vip==0 && temp.item_isrecommend ==0 && !(isEmpty(temp.password))){
                trs=trs+"<strong class='colorful' app>LOCK</strong>";
            }
            trs=trs+"</td>";

            // 信用等级
            trs=trs+"<td class='trust_range'>";
            if(temp.total>90){
                trs=trs+"A+";
            }
            if(temp.total>85 && temp.total<=90){
                trs=trs+"A";
            }
            if(temp.total>75 && temp.total<=85){
                trs=trs+"A-";
            }
            if(temp.total>65 && temp.total<=75){
                trs=trs+"B";
            }
            trs=trs+"</td>";

            // 担保机构
            trs=trs+"<td><image src='/img/logo.png'></image></td>";

            //  投资进度

            if(temp.item_status==1){
                trs=trs+"<td>"+"<strong class='countdown time' data-time='"+temp.syTime+"' data-item='"+temp.id+"'>";
                trs=trs+"<time class='hour'></time>";
                trs=trs+" &nbsp;:<time class='min'></time>";
                trs=trs+" &nbsp;:<time class='sec'></time>";
                trs=trs+"</strong></td>";
            }else{
                trs=trs+"<td class='data-scale' data-val='"+temp.item_scale+"'></td>";
            }
            // 操作项
            trs=trs+"<td>";
            var href=ctx+"/item/details?itemId="+temp.id;
            if(temp.item_status==1){
                trs=trs+"<p><a href='"+href+"'><input class='countdownButton' valid type='button' value='即将开标'></a></p>";
            }
            if(temp.item_status==10){
                trs=trs+"<p class='left_money'>可投金额"+temp.syAccount+"</p><p><a href='"+href+"'><input valid type='button' value='立即投资'></a></p>";
            }
            if(temp.item_status==20){
                trs=trs+"<p><a href='"+href+"'><input not_valid  type='button' value='已抢完'></a></p>"
            }
            if(temp.item_status==30 || temp.item_status==31){
                trs=trs+"<p><a ><input not_valid type='button' value='还款中'></a></p>";
            }
            if(temp.item_status==32){
                trs=trs+"<p><a href='"+href+"' class='yihuankuan'><input not_valid type='button' value='已还款'></a></p>";
            }
            if(temp.item_status==23){
                trs=trs+"<p><a href='"+href+"' ><input not_valid type='button' value='已满标'></a></p>";
            }
            trs=trs+"</td>";




            trs=trs+"</tr>";
        }
        console.log(trs);
        // 拼接节点trs 到指定节点下
        $("#pcItemList").html(trs);
    }
}

function initPageHtml(pages,currentPage) {
    var lis="";
    for(var i = 0;i<pages.length;i++){
        var p = pages[i];
        var href = "javascript:toPageData("+p+")";
        if(currentPage==p){
            lis=lis+"<li class='active'><a title='第"+p+"页'href='"+href+"' >"+p+"</a></li>";
        }else{
            lis=lis+"<li><a href='"+href+"' title='第"+p+"页' >"+p+"</a></li>";
        }
    }
    $("#pages").html(lis);
}

function initItemData(itemType) {
    var itemCycle;
    $(".tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle=$(this).index();
        }
    });
    var isHistory=0;
    if(itemCycle==4){
        isHistory=1;
    }
    loadInvestListData(itemCycle,itemType,isHistory);
}

function toPageData(pageNum) {
    var itemCycle;
    $(".tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle=$(this).index();
        }
    });
    var isHistory=0;
    if(itemCycle==4){
        isHistory=1;
    }
    var itemType=$("#itemType").val();
    loadInvestListData(itemCycle,itemType,isHistory,pageNum);
}

function initItemScale(){
    $(".data-scale").each(function () {
        $(this).radialIndicator({
            barColor: 'orange',
            barWidth: 5,
            roundCorner : true,
            // percentage: true,
            radius:50,
            format: '#%'
        });
        var radialObj = $(this).data('radialIndicator');
        radialObj.value($(this).attr("data-val"));
    })

}

function initItemSyTime() {
    $(".countdown").each(function () {
        var syTime= $(this).attr("data-time");
        var itemId=$(this).attr("data-item");
        timer(syTime,$(this),itemId);
    })
}

function timer(intDiff,obj,itemId){
    if( obj.timers){
        clearInterval(obj.timers);
    }

    obj.timers=setInterval(function(){
        var day=0,
            hour=0,
            minute=0,
            second=0;//时间默认值
        if(intDiff > 0){
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        }
        if (hour <= 9) hour = '0' + hour;
        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        obj.find('.hour').html(hour);
        obj.find('.min').html(minute);
        obj.find('.sec').html(second);
        intDiff--;
        if(intDiff==-1){
            $.ajax({
                url : ctx+'/item/updateBasItemStatusToOpen',
                dataType : 'json',
                type : 'post',
                data:{
                    itemId:itemId
                },
                success : function(data) {
                    if(data.code==200){
                        window.location.reload()
                    }
                },
                error : function(textStatus, errorThrown) {

                }
            });
        }
    }, 1000);
}
