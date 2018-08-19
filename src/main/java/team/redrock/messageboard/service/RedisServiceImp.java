package team.redrock.messageboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.redrock.messageboard.bean.Message;
import team.redrock.messageboard.config.RedisConfig;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


/**配置redis缓存的注解，该注解是用来开启声明的类参与缓存,
 * 如果方法内的@Cacheable注解没有添加key值，那么会自动使用cahceNames配置参数并且追加方法名。
 * */
//@CacheConfig(cacheNames = "msg")
@Service
public class RedisServiceImp implements RedisService {

    Message message = new Message();

    /**将数据以json方式保存*/
    /**1.将对象转json传入redis*/
    /**2.使用json序列化器*/

    //使用普通序列化器进行对象序列化（使用jdk序列化机制）
// @Resource
//   private RedisTemplate<String ,Message>  messageRedisTemplate ;


    //使用json序列化器将对象序列化成json形式（使用json序列化机制）
    @Autowired
    RedisTemplate<Object,Message> messageRedisTemplate;




  /**配置redis缓存的注解，该注解配置方法的缓存参数，
   * 可自定义缓存的key以及value。*/
//    @Cacheable
    @Override
    public Long submitMsg( String username, String msg){
        SimpleDateFormat f_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = f_date.format(new Date());


        Long messageId = this.messageRedisTemplate.opsForValue().increment("msgId:",1);  //实现id自增

        message.setMsg(msg);
        message.setUsername(username);
        message.setTime(date);


        this.messageRedisTemplate.opsForZSet().add("message",message,messageId);

/*      非对象存数据到redis
        String word = "word:" + messageId;
        HashMap<String,String> messageData = new HashMap<String,String>();
        messageData.put("username", username);
        messageData.put("msg", msg);
        messageData.put("date", date);

        messageRedisTemplate.opsForHash().putAll(word, messageData);
        */


        return messageId;

    }


@Override
    public List<Message> readMsg(){

//        Map<Object, Object> messageMap  = messageRedisTemplate.opsForHash().entries("message");
        List<Message> messageList = new ArrayList<>();
        Set<Message> msgs = this.messageRedisTemplate.opsForZSet().reverseRange("message",0,5);   //可以用于控制输出数量，现在控制输出6个
//    Set<Message> msgs = this.messageRedisTemplate.opsForZSet().rangeBy("message",0,-1);
        for(Message item :msgs ){
            messageList.add(item);
        }
                return messageList;

        }



}
