package com.tl666.loginregister.service;

import com.tl666.loginregister.pojo.User;

public interface UserService {
    /**
     * 添加用户的服务方法
     * @param user
     */
    void addUserService(User user);

    /**
     * 修改用户的服务方法
     * @param user
     */
    void updateUserService(User user);

    /**
     * 登录服务方法
     * @param user
     * @return 用户所有信息
     */
    User loginService(User user);

    /**
     * 效验用户服务方法
     * @param user
     * @return 用户所有信息
     */
    User checkUserService(String username);
}
