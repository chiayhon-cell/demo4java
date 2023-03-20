package cn.chiayhon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * simple state machine example
 */
@Configuration
@EnableStateMachine
public class SimpleStateMachineConfiguration
        extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial("INIT") // init state
                .end("END") // end state
                .states(new HashSet<>(Arrays.asList("S1","S2"))); // other state
    }

    /**
     * config the transition of states
     * @param transitions the {@link StateMachineTransitionConfigurer}
     * @throws Exception e
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal()
                .source("INIT").target("S1").event("E1").and()
                .withExternal()
                .source("S1").target("S2").event("E1").and()
                .withExternal()
                .source("S2").target("END").event("E1");
    }
}
