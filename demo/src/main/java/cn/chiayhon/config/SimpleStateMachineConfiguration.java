package cn.chiayhon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * simple state machine example
 */
@Configuration
@EnableStateMachine
public class SimpleStateMachineConfiguration
        extends StateMachineConfigurerAdapter<String, String> {
    @Autowired
    private List<StateMachineListenerAdapter<String,String>> stateMachineListeners;

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        super.configure(config);
        for (StateMachineListenerAdapter<String, String> machineListener : stateMachineListeners) {
            config.withConfiguration().listener(machineListener);
        }
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial("INIT") // init state
                .end("END") // end state
                .states(new HashSet<>(Arrays.asList("S1","S2"))) // other state

                // add entry/exit action as well as add corresponding error action
                .stateEntry("S3",entryAction(),errorAction())
                .state("S3",executeAction())
                .stateExit("S3", exitAction(), errorAction())

                // extended state
                .state("S4",executeAction());

    }

    /**
     * config the transition of states
     * @param transitions the {@link StateMachineTransitionConfigurer}
     * @throws Exception e
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal()
                .source("INIT").target("S1").event("E1").action(transitionAction()).and()
                .withExternal()
                .source("S1").target("S2").event("E1").action(transitionAction()).and()
                .withExternal()
                .source("S2").target("S3").event("E1").action(transitionAction()).and()
                .withExternal()
                .source("S3").target("S4").event("E1").action(transitionAction()).and()
                .withExternal()
                .source("S4").target("END").event("E1").action(transitionAction());
    }



    /**
     * init a action for transition
     * @return
     */
    @Bean
    public Action<String,String> transitionAction(){
        return context -> System.out.println("========== state[" + context.getSource().getId() + "] transit to state[" + context.getTarget().getId() + "] ==========");
    }

    /**
     * entry action for transition
     * @return
     */
    @Bean
    public Action<String,String> entryAction(){
        return context -> {
            System.out.println("this is a action for entry .source state = [" + context.getSource().getId() + "]" +" . target state = [" + context.getTarget().getId() + "]");
            if ((System.currentTimeMillis() & 1) == 1){
                throw new RuntimeException("unexpected exception");
            }
        };
    }

    /**
     * exit action for transition
     * @return
     */
    @Bean
    public Action<String,String> exitAction(){
        return context -> {
            System.out.println("this is a action for exit .source state = [" + context.getSource().getId() + "]" +" . target state = [" + context.getTarget().getId() + "]");
            if ((System.currentTimeMillis() & 1) == 1){
                throw new RuntimeException("unexpected exception");
            }
        };
    }

    /**
     * error action for state
     *
     * @return
     */
    @Bean
    public Action<String, String> errorAction() {
        return context -> System.out.println("error when executing .source state = [" + context.getSource().getId() + "]" + " . target state = [" + context.getTarget().getId() + "]");
    }

    /**
     * counter of the field approve
     *
     * @return
     */
    @Bean
    public Action<String, String> executeAction() {
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
