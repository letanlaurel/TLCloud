package com.tl666.emailcenter.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 普通邮件发送模板
 */
public enum  EmailUtils {
    TL;

    /**
     *
     * @param mailSender  邮箱发送器
     * @param email  目标邮箱
     * @param subject 标题
     * @param text  内容
     */
    public void EmailTemplet(JavaMailSenderImpl mailSender,String email,String subject,String text,String sendPerson){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(sendPerson); //表示发件人
        message.setTo(email);
        mailSender.send(message);
    }
}
