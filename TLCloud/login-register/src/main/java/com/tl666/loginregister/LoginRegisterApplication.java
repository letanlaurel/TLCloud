package com.tl666.loginregister;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@EnableRabbit
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.tl666.loginregister.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 300,redisNamespace = "${spring.session.redis.namespace}")
@EnableCaching
@ServletComponentScan("com.tl666.loginregister.verificationCode")
public class LoginRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginRegisterApplication.class, args);
    }

    /**
     * 配置可以从提供者那调用方法
     * @return
     */
    @LoadBalanced //开启负载均衡
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();

    }
}
