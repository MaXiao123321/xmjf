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
});