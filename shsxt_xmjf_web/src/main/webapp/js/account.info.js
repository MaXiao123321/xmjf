$(function () {
    loadAccountInfo();

    loadInvestIncomeInfo();

});


function loadAccountInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/account/countBusAccountInfoByUserId",
        dataType:"json",
        success:function (data) {
            var data1 = data.data1;
            var data2 = data.data2;


            $('#pie_chart').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    spacing : [100, 0 , 40, 0]
                },
                title: {
                    floating:true,
                    text: "总金额:"+data1+"￥"
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.y}￥',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        },
                        point: {
                            events: {
                                mouseOver: function(e) {  // 鼠标滑过时动态更新标题
                                    // 标题更新函数，API 地址：https://api.hcharts.cn/highcharts#Chart.setTitle
                                    chart.setTitle({
                                        text: e.target.name+ '\t'+ e.target.y + ' %'
                                    });
                                }
                            }
                        },
                    }
                },
                series: [{
                    type: 'pie',
                    innerSize: '80%',
                    name: '市场份额',
                    data: data2
                }]
            }, function(c) {
                // 环形图圆心
                var centerY = c.series[0].center[1],
                    titleHeight = parseInt(c.title.styles.fontSize);
                c.setTitle({
                    y:centerY + titleHeight/2
                });
                chart = c;
            });


        }
    })
}


function loadInvestIncomeInfo() {
        $.ajax({
            type:"post",
            url:ctx+"/invest/countInvestIncomeInfoByUserId",
            dataType:"json",
            success:function (data) {
                var data1=data.data1;
                var data2=data.data2;
                    Highcharts.chart('line_chart', {
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: '投资收益曲线图'
                    },
                    subtitle: {
                        text: '数据来源:小马金服'
                    },
                    xAxis:{
                        // List<String>
                        categories: data1
                    },
                    yAxis: {
                        title: {
                            text: '金额(￥)'
                        }
                    },
                    plotOptions: {
                        line: {
                            dataLabels: {
                                // 开启数据标签
                                enabled: true
                            },
                            // 关闭鼠标跟踪，对应的提示框、点击事件会失效
                            enableMouseTracking: false
                        }
                    },
                    series:data2
                });

            }
        })
}

