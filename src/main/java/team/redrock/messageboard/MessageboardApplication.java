package team.redrock.messageboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class MessageboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageboardApplication.class, args);
    }

    private RedisTemplate redisTemplate;
}
