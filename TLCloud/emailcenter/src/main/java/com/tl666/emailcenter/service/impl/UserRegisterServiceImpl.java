package com.tl666.emailcenter.service.impl;

import com.tl666.emailcenter.service.UserRegisterService;
import com.tl666.emailcenter.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    String sendPerson;
//    @Autowired
//    RabbitTemplate rabbitTemplate;


    @Override
    public String sendToEmailService(String email) {
        String code = UUID.randomUUID().toString().replaceAll("[-]", "").substring(1, 9);
        String text = "【TL科技】您本次注册的激活码为：" + code + "，切勿告诉他人！【有效期3分钟】";
        String subject = "【激活码】";
        EmailUtils.TL.EmailTemplet(mailSender, email, subject, text, sendPerson);
//        Map<String, Object> map = new HashMap<>();
//        map.put("registerCode", s);
//        map.put("email", email);
        //放入消息队列
        return code;
    }

    @Override
    public void UserRegisterSuccessService(String uname,String email) {
        String subject = "【TL科技】";
        String text = "【TL科技】您已成功在我们平台注册了账户，您的用户名为："+uname+"，感谢您的支持！";
        EmailUtils.TL.EmailTemplet(mailSender,email,subject,text,sendPerson);
    }

    @Override
    public String sendBackePWDEmail(String email) {
        String code = UUID.randomUUID().toString().replaceAll("[-]", "").substring(1, 9);
        String subject = "【TL科技】验证码";
        String text = "【TL科技】您本次找回密码的验证码为：" + code + "，切勿告诉他人！【有效期3分钟】";
        EmailUtils.TL.EmailTemplet(mailSender,email,subject,text,sendPerson);
        return code;
    }
}
