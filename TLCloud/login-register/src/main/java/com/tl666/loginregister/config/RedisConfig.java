package com.tl666.loginregister.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

//      @Bean
//      public RedisTemplate<String, Object> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
//          RedisTemplate<String, Object> template = new RedisTemplate<>();
//          template.setKeySerializer(new StringRedisSerializer());
//          template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//          template.setConnectionFactory(redisConnectionFactory);
//          return template;
//      }

//    @Value("${spring.redis.cluster.nodes}")
//    private String clusterNodes;
//
//    @Bean
//    public JedisCluster getJedisCluster(){
//        //分割集群节点
//        String[] cNodes = clusterNodes.split(",");
//        //创建set集合对象
//        Set<HostAndPort> nodes =new HashSet<>();
//        for (String node:cNodes) {
//            String[] hp = node.split(":");
//            nodes.add(new HostAndPort(hp[0],Integer.parseInt(hp[1])));
//        }
//        //创建Redis集群对象
//        JedisCluster jedisCluster=new JedisCluster(nodes);
//        return jedisCluster;
//    }

    @Bean
    public RedisTemplate<String, Object> userRedisTemplate(
            RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return template;
    }
}
