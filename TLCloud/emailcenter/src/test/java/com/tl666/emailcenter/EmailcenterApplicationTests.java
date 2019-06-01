package com.tl666.emailcenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailcenterApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;


    @Test
    public void test4(){
        //创建一个exchange
        Exchange exchange = new FanoutExchange("email.exchange");
        amqpAdmin.declareExchange(exchange);
        //创建一个队列
        amqpAdmin.declareQueue(new Queue("email.queue"));
        //绑定
        amqpAdmin.declareBinding(new Binding("email.queue", Binding.DestinationType.QUEUE,"email.exchange","tlgg",null));
    }

    /**
     * 点对点模式
     */
    @Test
    public void contextLoads() {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","java扔上去的东西，哈哈哈");
        map.put("hh", Arrays.asList("难受啊",12313,true));
        //对象被默认序列化后发送上去才用jdk的序列号，可以通过配置变成json序列化
        rabbitTemplate.convertAndSend("email.exchange","tlgg",map);
    }

    /**
     * 接受消息
     */
    @Test
    public void test1(){
        Message o = rabbitTemplate.receive("email.queue",2000l);
        System.out.println(o);
    }

    /**
     * 广播模式
     */
//    @Test
//    public void test2(){
//        User user = new User();
//        user.setId(1);
//        user.setName("乐歌歌");
//        user.setAge(21);
//        rabbitTemplate.convertAndSend("YL","",user);
//    }

}
