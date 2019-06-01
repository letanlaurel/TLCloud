package com.tl666.loginregister;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginRegisterApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    HttpServletRequest request;
    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("aa","难受");
        System.out.println(redisTemplate.opsForValue().get("aa"));
//        request.getSession().setAttribute("tl","乐哥哥");
//        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
//        while (attributeNames.hasMoreElements()){
//            System.out.println(attributeNames.nextElement());
//        }
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void test(){
        Map<String,Object> map = new HashMap<>();
        map.put("uname","1700130417");
        map.put("email","1976087502@qq.com");
        rabbitTemplate.convertAndSend("email.topic","email.info.topic",map);
    }

}
