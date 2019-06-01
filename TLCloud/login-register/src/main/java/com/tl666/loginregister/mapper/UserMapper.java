package com.tl666.loginregister.mapper;

import com.tl666.loginregister.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    /**
     * 添加用户
     * @param user
     */
    @Insert("insert into user (username,pwd,email,name,createTime) values(#{username},#{pwd},#{email},#{name},#{createTime})")
    void addUser(User user);

    /**
     * 修改用户
     * @param user
     */
    @Update("update user set pwd = #{pwd} where username = #{username}")
    void updateUser(User user);

    /**
     * 用户登录
     * @param user
     * @return 用户所有信息
     */
    @Select("select * from user where username = #{username} and pwd = #{pwd}")
    User login(User user);

    /**
     * 判断用户是否存在
     * @param username
     * @return 用户所有信息
     */
    @Select("select * from user where username = #{username}")
    User checkUser(String username);
}
