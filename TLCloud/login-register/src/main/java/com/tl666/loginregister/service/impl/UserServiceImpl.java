package com.tl666.loginregister.service.impl;

import com.tl666.loginregister.mapper.UserMapper;
import com.tl666.loginregister.pojo.User;
import com.tl666.loginregister.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public void addUserService(User user) {
        userMapper.addUser(user);
    }

    @Override
   // @CachePut   //先调用目标方法，将目标方法的结果缓存起来
    public void updateUserService(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public User loginService(User user) {
        return userMapper.login(user);
    }

    @Override
  //  @Cacheable(value = "checkUser",key = "#args[0]",sync = true)
    public User checkUserService(String username) {
        return userMapper.checkUser(username);
    }
}
