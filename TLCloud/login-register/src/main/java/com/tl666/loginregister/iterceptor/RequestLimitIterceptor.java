package com.tl666.loginregister.iterceptor;

import com.tl666.loginregister.annotation.TLRequestLimit;
import com.tl666.loginregister.utils.GetIpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Timer;
import java.util.TimerTask;


@CrossOrigin(origins="*",maxAge=3600)
@Component
public class RequestLimitIterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisTemplate redisTemplate;

   // Map<String, Integer> map = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法中的注解,看是否有该注解
            TLRequestLimit tLRequestLimit = hm.getMethodAnnotation(TLRequestLimit.class);
            if (tLRequestLimit == null) {
              //  System.out.println("放行。。。");
                return true;
            }
            long seconds = tLRequestLimit.time();
            int maxCount = tLRequestLimit.count();
            String ip = GetIpUtil.getIpAddr(request);
            ValueOperations map = redisTemplate.opsForValue();
            String url = request.getRequestURL().toString();
            //将每个用户访问的记录在redis里面
            if (map.get(ip) == null || map.get(ip).toString() == "0") {
                map.set(ip, 1);
            } else {
                map.set(ip, Integer.parseInt(map.get(ip).toString()) + 1);
            }
            int count = Integer.parseInt(map.get(ip).toString());
            if (count > 0) {
                //创建一个定时器
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
//                        map.remove(key);
                        redisTemplate.delete(ip);
                    }
                };
                //这个定时器设定在time规定的时间之后会执行上面的remove方法，也就是说在这个时间后它可以重新访问
                timer.schedule(timerTask, seconds);
            }
            System.out.println(count);
            if (count > maxCount) {
                System.out.println("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + maxCount + "]");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}
