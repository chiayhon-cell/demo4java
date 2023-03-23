package cn.chiayhon.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleStateMachineDemo implements ApplicationRunner {


    @Autowired
    private StateMachine<String,String> stateMachine;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        stateMachine.start();
    }

    //    @EventListener(ApplicationReadyEvent.class)
    public void start1() {
        State<String, String> state = stateMachine.getState();
        log.info("current state : {}", state);
        stateMachine.sendEvent("E1");
        State<String, String> state1 = stateMachine.getState();
        log.info("current state : {}", state1);
    }



    @EventListener(ApplicationReadyEvent.class)
    public void start2() {
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent("E1");
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent("E1");
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent("E1");
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent("E1");
        log.info("current state : {}", stateMachine.getState());
    }


}
