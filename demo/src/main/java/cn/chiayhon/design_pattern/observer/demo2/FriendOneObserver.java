package cn.chiayhon.design_pattern.observer.demo2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FriendOneObserver implements Observer {

    @Override
    public void update(String message) {
        // 模拟处理业务逻辑
        log.info("FriendOne 知道了你发动态了:" + message);
    }
}
