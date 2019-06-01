package com.tl666.loginregister.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id; //用户id 唯一标识
    private String username; //账号
    private String pwd; //密码
    private String email;//邮箱
    private String name;//昵称
    private Date createTime;//创建时间
}
