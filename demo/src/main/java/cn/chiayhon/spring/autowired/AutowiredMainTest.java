package cn.chiayhon.spring.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AutowiredMainTest implements ApplicationRunner {

    @Autowired(required = false)
    private AutowiredConstructTest1 test1;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        print();
    }

    private void print() {
        System.out.println(test1);
    }
}
