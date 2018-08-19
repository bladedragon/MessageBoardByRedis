package team.redrock.messageboard.bean;

import lombok.Data;



import java.io.Serializable;


@Data
public class Message implements Serializable {

    private String msg;
    private String time;
    private String username;

}
