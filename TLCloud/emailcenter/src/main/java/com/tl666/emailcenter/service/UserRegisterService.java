package com.tl666.emailcenter.service;

public interface UserRegisterService {
    //注册码邮件
    String sendToEmailService(String email);
    //注册完成邮件
    void UserRegisterSuccessService(String uname,String email);

    String sendBackePWDEmail(String email);
}
