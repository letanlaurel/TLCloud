package com.tl666.emailcenter.handler;

import com.tl666.emailcenter.service.UserRegisterService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class UserRegisterHandler {

    @Autowired
    UserRegisterService userRegisterService;

//    @Autowired
//    RabbitTemplate rabbitTemplate;

    @RequestMapping("sendRegisterEmail")
    public String sendRegisterEmail(@RequestBody Map map){
        String email = map.get("email").toString();
        return userRegisterService.sendToEmailService(email);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${mq.config.queue}",autoDelete = "false"),//不是临时队列
                    exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
                    key = "${mq.config.routing.key}"
            )
    )
    //被上面注解修饰的方法不能用返回值，注意了否则报错
    public void sendRegisterSuccessEmail(Message message) throws IOException {
        byte[] body = message.getBody();
        String s = new String(body);
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> parseMap = jsonParser.parseMap(s);
        String uname = parseMap.get("uname").toString();
        String email = parseMap.get("email").toString();
        userRegisterService.UserRegisterSuccessService(uname,email);
    }

    @RequestMapping("sendBackePWDEmail")
    public String sendBackePWDEmail(@RequestBody Map map){
        String email = map.get("email").toString();
        return userRegisterService.sendBackePWDEmail(email);
    }

}
