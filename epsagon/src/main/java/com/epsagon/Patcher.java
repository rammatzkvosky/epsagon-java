package com.epsagon;

import com.epsagon.instrumentation.EpsagonInstrumentation;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;
import java.util.ServiceLoader;

import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class Patcher {
    public static void instrumentAll(Instrumentation inst) {
        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .ignore(
                        not(nameStartsWith("com.amazonaws"))
                );

            for (final EpsagonInstrumentation instrumenter : ServiceLoader.load(EpsagonInstrumentation.class)) {
                agentBuilder = instrumenter.instrument(agentBuilder);
            }
        agentBuilder.installOn(inst);
    }
}
