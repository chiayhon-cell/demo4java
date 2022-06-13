package cn.chiayhon.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
@Slf4j
public class RestControllerDemo {
    @GetMapping
    public String get() {
        String message = "you get my data!";
        log.info(message);
        return message;
    }
}
