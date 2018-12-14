package com.shsxt.xmjf.api.constants;


import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091900547295";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCNnas6kWbPho/azvJo6XmvtPPDzOkhK00rZ06xXOxK2LLk6n008g4+vLTAazcYzBJc+GocvGk2Ign1TGuTpFXBty4G9uQ0bqSnpU1dmzzWRScXSdKa/fKxSdXnFIEAtdpkyNMDjW/pBLf/YwcpIugARPgtyhDHg7HhTn41GutRR8RgQpUjKpqyN9UGylg9+RTLimnpSfvPM+w41RWL5ELXr71CJalxWNhWQRju8NZyHiy3el3fCEP/isGvltSTXt8DPsxqRLcX4jj7VpRVB7cS5zTuy92H2rINbBeBhmHIxpUnf8h/GQrs+hoGMK03BmMEXAw46ipSaPHC6s67rM4tAgMBAAECggEAWDeEp8pVyibfzAUoWpQ+WmcVhr5J54Cq5MPI8JGbHJDa9+DeuDGlEA3LZyYiv064jbOtnlL8E12m6Gh/hwxS44ZxpKL9y2+1ID3H8Oy4PB392PMCv8w795awKe/K4J9n+WpkkIz1zqlC2VNd7ZEyBqDwHM4IH9D46QpVck1fk1R8Vc/BHxfVVFIDsVt4/YGUvZAExq/6asY2T3grMF/hGBMnyP1gfe1cNo3FQUW43lx1fITlbE9lwYM2BSVNZx/g9wJziep6B1KQc/6mbOfEyjoSTvda/AAMAIr78N6UHVGcUGnNhL56kyiLl1OED5cvv01lxhyoBzRw1Oplb/hVgQKBgQDQvW7/+DsHXvT7R1mtyRpsUwpiYckVx1rTyUyZkR3D7qpx0sS1W6P+iaHGdhW2af0l/iOzwONHVWYDHYXwRo81sMTVmRtI9EN31HL9sg+4SgfCTbSAufdeP1kETae7vtx7/efwqdjFTAADOtY6GUn6/tjwmrp8mlcnIvMLejxsIQKBgQCtrblvNlrh/WNyB1UPaHtKorJrGmOgXXa6Ggxj0lKnnqSKBY3N9Qy7uckN/WfM5qd1Mch6PWrcUJejddIk4QRCW8eMM2q1TmN96HUllEmym85x042tgwQaqByqhWD9o/4kQuts3VJJHhnez4XL3tom+qaC98XHV4LFF4d6Wb1AjQKBgGaoc3mxtB4o5iTab1FXsQYLRPUjkEiEtbeorWJtCw18SBkYhiOHQT0CpSw8kOsYbyaLOTXDLEO9zSDFGUUU/oNyPyupU9DqSHAbbL42HyMMTYqSuq4AR5bzYq0KH+7mq/rS4Ynrji9h9DOicQ87trWZNkoGXqa/JRObTZqccDDhAoGAJI14hoh5DjEHGfujElAaCrY9OL9gEmM5Vqeeze95B+Ainw3WL2+Qk33ha7lWfFBbFBY7LEb6041+rJ9ib/KPs+ufDsnPfoMV+2zOIu+XpyiVhW1R1x/I4FdbWSQjKp62ISmYZNwbslnMdhFVBnhLj4KXQt8k1/R6CEMQd1altw0CgYA5WmOePSMkS+1bWTBqVJM/JJ6pqmxGok0WK6yiIt5uFtKvy3j238FAqBogFSYxOmHAdqYa3hk1Bj3BVehJXoZ1PoF9mf7yIt28v1FpmrlQj2u+aPx/78LOkWHLueAEmmF6HgBpYDVPeqhojGZLl86tyGKZwKzLets60RZc3p4Yag==";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq3scaMBvNsS5o/vjC/jmPAsWNInjvhkRtf0O1pJNZOd83Td8lVzml4iUltQfZKwxvrHRlBB0Pxh9WrRG+cBtRgralXwM3YK3Vh48e+AkFivXbMWumhMai0RumtXmerkQM12hBc/KXgGQYlVsiCoOeVzbjPUkGhn7+DWKUfJ37ULFKtv1801DqMFTIzoaoKxaoJctFagG3aEqjSDjQ1oF30HaB+dHNlQupy9/WNqG4MH3BAWhGkKvqhfD2mMo1ySIHZV4/elldepoCYlJNfFTybT7+ckJFBHgg6fO0qzVKMFBG7SSPEf2XsvV40o3DFRZAscG+am73RHZJXJRF4kEswIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://gqpsda.natappfree.cc/account/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://gqpsda.natappfree.cc/account/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public static final String seller_id="2088102176325454";

    public static final String trade_status="TRADE_SUCCESS";
    //public static final String notify_result="success";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
