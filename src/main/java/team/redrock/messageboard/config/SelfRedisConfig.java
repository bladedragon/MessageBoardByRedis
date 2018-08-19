//package team.redrock.messageboard.config;
//
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import team.redrock.messageboard.bean.Message;
//
//@Configuration
//public class SelfRedisConfig extends CachingConfigurerSupport {
//
//
//        @Bean
//        public RedisTemplate<String, Message> getRedisTemplate(
//                RedisConnectionFactory factory) {
//            RedisTemplate<String, Message> redisTemplate = new RedisTemplate<String, Message>();
//            redisTemplate.setConnectionFactory(factory);
                                                                             //下面两句可以直接用setDefaultSerializer代替
//            redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
//            redisTemplate.setValueSerializer(new RedisObjectSerializer()); // value的序列化类型
//            return redisTemplate;
//        }
//
//}
