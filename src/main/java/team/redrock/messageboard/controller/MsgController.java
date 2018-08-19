package team.redrock.messageboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.redrock.messageboard.bean.Message;
import team.redrock.messageboard.service.RedisService;

@Controller
@RequestMapping("/message")
public class MsgController {

    @Autowired
    RedisService redisService;

    @GetMapping
    public String messageForm(Model model) {

        Message message = new Message();
        model.addAttribute("message",message);               //必须加这个，使得post传入的数据有一个容器
        model.addAttribute("msglist",redisService.readMsg());

        return "msgboard";
    }

    @PostMapping
    public String messageSubmit(@ModelAttribute(value = "message") Message message) {

        redisService.submitMsg(message.getUsername(),message.getMsg());

        return "redirect:/message";             //重定向，使页面刷新
    }




}
