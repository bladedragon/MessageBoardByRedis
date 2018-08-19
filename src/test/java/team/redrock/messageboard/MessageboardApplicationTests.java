package team.redrock.messageboard;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import team.redrock.messageboard.bean.Message;
import team.redrock.messageboard.service.RedisService;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageboardApplicationTests {



    @Autowired
    RedisService redisService;
    @Test
    public void contextLoads() {


       // System.out.println( redisService.submitMsg("竹祯铮","你好呀！"));
        List<Message> test = redisService.readMsg();

       for(Message message: test){
           System.out.println(message.getMsg());
           System.out.println(message.getUsername());
           System.out.println(message.getTime());
       }
    }

}
