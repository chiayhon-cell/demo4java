package cn.chiayhon.base.concurrent.exchange;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Exchanger;

@Slf4j
public class ExchangeDemo {
    public static void main(String[] args) throws InterruptedException {
        final Exchanger<Boolean> booleanExchanger = new Exchanger<>();
        new Thread(() -> {
            try {
                Boolean exchange = booleanExchanger.exchange(true);
                log.info("1交换的结果:{}", exchange);
                log.info("子线程1结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        log.info("准备交换");
        final Boolean exchange = booleanExchanger.exchange(null);
        log.info("主交换的结果:{}", exchange);
        log.info("主线程结束");
    }
}
