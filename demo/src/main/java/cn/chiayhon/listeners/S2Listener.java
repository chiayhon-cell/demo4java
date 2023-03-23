package cn.chiayhon.listeners;

import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class S2Listener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        String source = from.getId();
        if (!Objects.equals(source, "S2")){
            return;
        }
        String target = to.getId();
        System.out.println("this is S2 listener !!! : source = " + source + " target = " + target);
    }
}
