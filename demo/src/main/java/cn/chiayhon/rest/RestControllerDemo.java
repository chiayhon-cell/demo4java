package cn.chiayhon.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class RestControllerDemo {
    @GetMapping
    public String get() {
        String message = "you get my data!";
        System.out.println("message=" + message);
        return message;
    }
}
