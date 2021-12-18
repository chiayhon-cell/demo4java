package cn.chiayhon.concurrent.threadpool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Class3 implements Runnable {

    @Override
    public void run() {
        log.info("开始执行");
    }
}