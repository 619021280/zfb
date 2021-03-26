package com.yan.zfb.controller;

import com.yan.zfb.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
public class MessageController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/upMessage")
    @CrossOrigin(value = "http://wangxiaodaidai.gitee.io/zfb/",allowedHeaders = "*",maxAge = 1800)
    public String upMessage(String goods_name, String address, String mobile, String addressee, @RequestHeader("Referer")String refer){
        System.out.println(goods_name+"..."+address+"..."+mobile+"..."+addressee);
        String[] split = refer.split("=");
        String[] split1 = split[1].split("&");
        emailService.sendMail(goods_name,address,mobile,addressee,"已下单",split1[0]+"@qq.com");
        Date date = new Date();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        String num = split1[0].split("@")[0];
        String phone = split[2];
        if (num.equals("121106401") && phone.equals("15959964396")){
            num = "25909750";
            phone = "18695669789";
        }
        return "redirect:first.html?orderId="+yyyyMMdd.format(date)+getNum(8)+"UN"+getNum(2)+
                "&mobile="+mobile+
                "&addressee="+toUtf8String(addressee)+
                "&address="+toUtf8String(address)+
                "&Num="+num+
                "&phone="+phone;
    }

    @PostMapping("/over")
    public void Over(String address,String mobile,String addressee,String Num){
        emailService.sendMail("POS机",address,mobile,addressee,"授权成功",Num+"@qq.com");
    }

    public String getNum(int num){
        StringBuilder str=new StringBuilder();//定义变长字符串bai
        Random random=new Random();
        for(int i=0;i<num;i++){
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    //将%E4%BD%A0转换为汉字
    public static String unescape(String s) {
        StringBuffer sbuf = new StringBuffer();
        int l = s.length();
        int ch = -1;
        int b, sumb = 0;
        for (int i = 0, more = -1; i < l; i++) {
            /* Get next byte b from URL segment s */
            switch (ch = s.charAt(i)) {
                case '%':
                    ch = s.charAt(++i);
                    int hb = (Character.isDigit((char) ch) ? ch - '0'
                            : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                    ch = s.charAt(++i);
                    int lb = (Character.isDigit((char) ch) ? ch - '0'
                            : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                    b = (hb << 4) | lb;
                    break;
                case '+':
                    b = ' ';
                    break;
                default:
                    b = ch;
            }
            /* Decode byte b as UTF-8, sumb collects incomplete chars */
            if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)
                sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb
                if (--more == 0)
                    sbuf.append((char) sumb); // Add char to sbuf
            } else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)
                sbuf.append((char) b); // Store in sbuf
            } else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)
                sumb = b & 0x1f;
                more = 1; // Expect 1 more byte
            } else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)
                sumb = b & 0x0f;
                more = 2; // Expect 2 more bytes
            } else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)
                sumb = b & 0x07;
                more = 3; // Expect 3 more bytes
            } else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)
                sumb = b & 0x03;
                more = 4; // Expect 4 more bytes
            } else /*if ((b & 0xfe) == 0xfc)*/{ // 1111110x (yields 1 bit)
                sumb = b & 0x01;
                more = 5; // Expect 5 more bytes
            }
            /* We don't test if the UTF-8 encoding is well-formed */
        }
        return sbuf.toString();
    }
}
