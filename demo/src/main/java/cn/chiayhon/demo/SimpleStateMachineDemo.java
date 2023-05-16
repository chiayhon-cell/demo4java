package cn.chiayhon.demo;

import cn.chiayhon.enums.EVENTS;
import cn.chiayhon.enums.STATES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleStateMachineDemo {


    @Autowired
    private StateMachine<STATES, EVENTS> stateMachine;

    @EventListener(ApplicationReadyEvent.class)
    public void start2() {
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent(EVENTS.EVENT1);
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent(EVENTS.EVENT1);
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent(EVENTS.EVENT1);
        log.info("current state : {}", stateMachine.getState());
        stateMachine.sendEvent(EVENTS.EVENT1);
        log.info("current state : {}", stateMachine.getState());

        ExtendedState extendedState = stateMachine.getExtendedState();
        Integer count = extendedState.get("approvalCount",Integer.class);
        log.info("approvalCount = {}", count);
    }


}
