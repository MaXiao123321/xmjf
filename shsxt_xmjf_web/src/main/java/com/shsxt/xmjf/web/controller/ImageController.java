package com.shsxt.xmjf.web.controller;

import com.google.code.kaptcha.Producer;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class ImageController {

    @Resource
    private Producer producer;

    @GetMapping("image")
    public void createImage(HttpServletRequest request, HttpServletResponse response){
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        //图片验证码
        String code = producer.createText();
        System.out.println("验证码值:"+code);
        //获取图像
        BufferedImage bi = producer.createImage(code);
        //通过response获取输出流输出
        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            //存储验证码到当前对象
            request.getSession().setAttribute(XmjfConstant.SESSION_IMAGE,code);
            //通过ImageIo类将图片显示给前台
            ImageIO.write(bi,"jpg",sos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(sos!=null){
                try {
                    sos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
