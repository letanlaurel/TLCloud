package com.tl666.loginregister.handler;

import com.tl666.loginregister.annotation.TLRequestLimit;
import com.tl666.loginregister.pojo.User;
import com.tl666.loginregister.service.UserService;
import com.tl666.loginregister.utils.GetIpUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins="*",maxAge=3600)
@RestController
@RequestMapping("UserHandler")
public class UserHandler {
    @Autowired
    UserService userService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @RequestMapping("login")
    public User userLogin(User user){
        User u = userService.loginService(user);
        if(u != null) {
            return u;
        }else{
            return null;
        }
    }

    /**
     * 添加用户
     * @param user
     * @param request
     * @return
     */
    @RequestMapping("addUser")
    public Map addUserHandler(User user,HttpServletRequest request) {
        String registercode = request.getParameter("registercode");
        String o = (String)redisTemplate.opsForValue().get(user.getEmail() + "code");
        Map<String,Object> map = new HashMap();
        if(o == null){
            //注册码无效或已过期
            map.put("msg","guoqi");
        }else if(registercode.equalsIgnoreCase(o)){
            user.setCreateTime(new Date());
            user.setName(user.getUsername());
            userService.addUserService(user);
            map.put("msg","success");
            map.put("uname",user.getUsername());
            map.put("email",user.getEmail());
            rabbitTemplate.convertAndSend("email.topic","email.info.topic",map);
        }else{
            map.put("msg","code_error");
        }
        return map;
//        Message message =  rabbitTemplate.receive("emailmsg.queue");
//        String emstr = new String(message.getBody());
//        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        Map<String, Object> parseMap = jsonParser.parseMap(emstr);
//        Map<String,Object> map = new HashMap();
//        System.out.println(map);
//        for (Map.Entry<String,Object> entry : map.entrySet()) {
//            if(entry.getValue().equals(registercode) && parseMap.get("").equals(user.getEmail())) {
//                user.setCreateTime(new Date());
//                user.setName(user.getUsername());
//                userService.addUserService(user);
//                map.put("msg","success");
//            }else{
//                map.put("msg","error");
//            }
//    }

    }

    /**
     * 发送注册邮件验证码
     * @param request
     */
    @TLRequestLimit(count = 1)
    @RequestMapping("sendEmail")
    public void sendEmail(HttpServletRequest request){
        String email = request.getParameter("email");
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        String s = restTemplate.postForObject("http://emailcenter/sendRegisterEmail",map, String.class);
//        rabbitTemplate.convertAndSend("email.exchange","tlgg",email);
        redisTemplate.opsForValue().set(email+"code",s,180, TimeUnit.SECONDS);//设置120秒过期
//        System.out.println(redisTemplate.opsForValue().get("g"));
        }

    /**
     * 验证用户是否存
     * @param request
     * @return
     */
    @TLRequestLimit(count = 10)
    @RequestMapping("checkUser")
    public Map checkUser(HttpServletRequest request){
        String username = request.getParameter("username");
        String checkc = request.getParameter("checkc");
        System.out.println(username);
        System.out.println(checkc);
        Map<String,Object> map = new HashMap<>();
        if(!checkc.equalsIgnoreCase((String) request.getServletContext().getAttribute("checkCode"))){
            map.put("msg","errcode");
        }else {
            User u= userService.checkUserService(username);
            if(u != null) {
                map.put("msg","success");
                map.put("email",u.getEmail());
            }else{
                map.put("msg","unexist"); //表示用户不存在
            }
        }
        return map;
    }

    @RequestMapping("tlgg")
    public Map tlgg(){
        Map<String,Object> map = new HashMap<>();
        map.put("msg","xiuxi");
        return map;
    }


    @TLRequestLimit(count = 10)
    @RequestMapping("registCheckUser")
    public Map registCheckUser(HttpServletRequest request){
        String ipAddr = GetIpUtil.getIpAddr(request);

        String username = request.getParameter("username");
        Map<String,Object> map = new HashMap<>();
        if(username != null) {
            User u = userService.checkUserService(username);
            if(u != null) {
                map.put("msg","error");
            }else{
                map.put("msg","success");
            }
        }
        return map;
    }

    /**
     * 发送找回密码邮件
     * @param request
     */
    @TLRequestLimit(count = 1)
    @RequestMapping("backUserToEamil")
    public void backUserToEamil(HttpServletRequest request){
        String email = request.getParameter("email");
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        String code = restTemplate.postForObject("http://emailcenter/sendBackePWDEmail",map, String.class);
        redisTemplate.opsForValue().set("backcode"+email,code,180, TimeUnit.SECONDS);//设置120秒过期
    }


//    @RequestMapping("tlgg")
//    public void getIP(HttpServletRequest request){
//        String ipAddr = GetIpUtil.getIpAddr(request);
//        System.out.println(ipAddr);
//    }

    /**
     * 修改用户密码
     * @param user
     * @param request
     * @return
     */
    @TLRequestLimit(count = 2)
    @RequestMapping("updateUser")
    public Map updateUser(User user,HttpServletRequest request){
        System.out.println(user);
        String fcode = request.getParameter("forget-code");
        String code = (String)redisTemplate.opsForValue().get("backcode"+user.getEmail());
        Map<String,Object> map = new HashMap<>();
        if(code.equals(fcode)){
            userService.updateUserService(user);
            map.put("msg","success");
        }else{
            map.put("msg","code_err");
        }
        return map;
    }

}
