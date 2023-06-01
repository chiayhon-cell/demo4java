package cn.chiayhon.listeners;

import cn.chiayhon.enums.EVENTS;
import cn.chiayhon.enums.STATES;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class S2Listener extends StateMachineListenerAdapter<STATES, EVENTS> {

    @Override
    public void stateChanged(State<STATES, EVENTS> from, State<STATES, EVENTS> to) {
        if (from == null){
            return;
        }
        STATES source = from.getId();
        if (!Objects.equals(source, STATES.STATE2)){
            return;
        }
        STATES target = to.getId();
        System.out.println("this is S2 listener !!! : source = " + source + " target = " + target);
    }
}
