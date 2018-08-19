package team.redrock.messageboard.service;

import team.redrock.messageboard.bean.Message;

import java.util.List;

public interface RedisService {
    public List<Message> readMsg();

    public Long submitMsg( String username, String msg);
}
