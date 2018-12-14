<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>充值</title>
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/recharge.css">
    <link rel="stylesheet" href="/css/user_siderbar.css">
    <script type="text/javascript">
        var ctx="${ctx}";
    </script>

</head>
<body>
<#include "include/header.ftl">
<div class="container clear">
<#include "include/user_siderbar.ftl">
    <div class="content fr">
        订单支付成功!3秒后跳转,没有跳转请点击<a href="${ctx}/account/rechargeRecord">这里</a>
        <script type="text/javascript">
            var ctx="${ctx}";
            setTimeout(function () {
                window.location.href=ctx+"/account/rechargeRecord";
            },3000)
        </script>
    </div>

</div>
</body>
</html>