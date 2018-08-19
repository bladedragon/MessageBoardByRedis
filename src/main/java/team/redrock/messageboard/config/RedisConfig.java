package team.redrock.messageboard.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import team.redrock.messageboard.bean.Message;

import java.net.UnknownHostException;

@Configuration
public class RedisConfig {  //构造属于自己的序列化器
    @Bean
    public RedisTemplate<Object, Message> messageRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Message> template = new RedisTemplate<Object, Message>();
        template.setConnectionFactory(redisConnectionFactory);
        //使用json的序列化器
        Jackson2JsonRedisSerializer ser = new Jackson2JsonRedisSerializer<Message>(Message.class);
        template.setDefaultSerializer(ser);                 //相当于key的序列化类型和value的序列化类型
        return template;
    }


}
