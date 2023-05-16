package cn.chiayhon.config;

import cn.chiayhon.enums.EVENTS;
import cn.chiayhon.enums.STATES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.ConfigurationConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static cn.chiayhon.enums.EVENTS.EVENT1;
import static cn.chiayhon.enums.STATES.*;

/**
 * simple state machine example
 */
@Configuration
@EnableStateMachine
@Slf4j
public class SimpleStateMachineConfiguration
        extends StateMachineConfigurerAdapter<STATES, EVENTS> {

    @Autowired
    private List<StateMachineListenerAdapter<STATES,EVENTS>> stateMachineListeners;

    @Override
    public void configure(StateMachineConfigurationConfigurer<STATES, EVENTS> config) throws Exception {
        super.configure(config);
        ConfigurationConfigurer<STATES, EVENTS> configurer = config.withConfiguration();
        configurer.autoStartup(true); // auto start after application running
        for (StateMachineListenerAdapter<STATES, EVENTS> machineListener : stateMachineListeners) {
            configurer.listener(machineListener);
        }

    }

    @Override
    public void configure(StateMachineStateConfigurer<STATES, EVENTS> states) throws Exception {
        states.withStates()
                .initial(INIT) // init state
                .end(END) // end state
                .states(new HashSet<>(Arrays.asList(STATE1, STATE2))) // other state

                // add entry/exit action as well as add corresponding error action
                .stateEntry(STATE3, entryAction(), errorAction())
                .state(STATE3, executeAction())
                .stateExit(STATE3, exitAction(), errorAction())

                // extended state
                .state(STATE4, executeAction());

    }

    /**
     * config the transition of states
     * @param transitions the {@link StateMachineTransitionConfigurer}
     * @throws Exception e
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<STATES, EVENTS> transitions) throws Exception {
        transitions.withExternal()
                .source(INIT).target(STATE1).event(EVENT1).action(transitionAction()).and()
                .withExternal()
                .source(STATE1).target(STATE2).event(EVENT1).action(transitionAction()).and()
                .withExternal()
                .source(STATE2).target(STATE3).event(EVENT1).action(transitionAction()).and()
                .withExternal()
                .source(STATE3).target(STATE4).event(EVENT1).action(transitionAction()).and()
                .withExternal()
                .source(STATE4).target(STATE5).event(EVENT1).action(transitionAction()).and();
    }



    /**
     * init a action for transition
     * @return
     */
    public Action<STATES, EVENTS> transitionAction(){
        return context -> System.out.println("========== state[" + context.getSource().getId() + "] transit to state[" + context.getTarget().getId() + "] ==========");
    }

    /**
     * entry action for transition
     * @return
     */
    public Action<STATES, EVENTS> entryAction(){
        return context -> {
            System.out.println("this is a action for entry .source state = [" + context.getSource().getId() + "]" +" . target state = [" + context.getTarget().getId() + "]");
            if ((System.currentTimeMillis() & 3) == 3){
                throw new RuntimeException("unexpected exception");
            }
        };
    }

    /**
     * exit action for transition
     * @return
     */
    public Action<STATES, EVENTS> exitAction(){
        return context -> {
            System.out.println("this is a action for exit .source state = [" + context.getSource().getId() + "]" +" . target state = [" + context.getTarget().getId() + "]");
            if ((System.currentTimeMillis() & 3) == 3){
                throw new RuntimeException("unexpected exception");
            }
        };
    }

    /**
     * error action for state
     *
     * @return
     */
    public Action<STATES, EVENTS> errorAction() {
        return context -> System.out.println("error when executing .source state = [" + context.getSource().getId() + "]" + " . target state = [" + context.getTarget().getId() + "]");
    }

    /**
     * counter of the field approve
     *
     * @return
     */
    public Action<STATES, EVENTS> executeAction() {
        return context -> {
            System.out.println("execute counting .source state = [" + context.getSource().getId() + "]" + " . target state = [" + context.getTarget().getId() + "]");
            int approvals = (int) context.getExtendedState().getVariables()
                    .getOrDefault("approvalCount", 0);
            approvals++;
            context.getExtendedState().getVariables()
                    .put("approvalCount", approvals);
        };
    }
}
