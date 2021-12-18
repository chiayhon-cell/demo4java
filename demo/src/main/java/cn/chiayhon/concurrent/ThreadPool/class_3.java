package cn.chiayhon.concurrent.ThreadPool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class class_3 implements Runnable{

    @Override
    public void run() {
        log.info("开始执行");
    }
}