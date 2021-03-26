package com.yan.zfb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async("taskExecutor")
    public void sendMail(String goods_name, String address, String mobile, String addressee, String states,String receiver){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("2531455901@qq.com");
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setSubject("新订单");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        simpleMailMessage.setText(
                "货物名称:"+goods_name+";"+
                "地址:"+address+";"+
                "手机号:"+mobile+";"+
                "收货地址:"+addressee+";"+
                "下单时间:"+format+";"+
                "订单状态:"+states+"。"
        );
        mailSender.send(simpleMailMessage);
    }
}
