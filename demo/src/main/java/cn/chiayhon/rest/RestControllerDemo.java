package cn.chiayhon.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
@Slf4j
public class RestControllerDemo {

    @GetMapping("/{num}")
    public String get(@PathVariable("num") Long num) {
        log.info("num:{}", num);
        String message = "you get my data!! 【data=" + num + "】";
        log.info(message);
        return message;
    }
}
