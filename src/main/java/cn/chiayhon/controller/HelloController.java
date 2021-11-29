package cn.chiayhon.controller;

import cn.chiayhon.bean.User;
import cn.chiayhon.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
@Slf4j
public class HelloController {
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String sayHello() {
        return "index";
    }

    @PostMapping(path = "/query")
    public String query(Long id){
        User user = userService.queryById(id);
        log.info("user={}",user);
        return "success";
    }
}