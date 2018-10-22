package com.epsagon.instrumentation;

import com.epsagon.EpsagonRequestHandler;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Map;

public abstract class EpsagonInstrumentation {
    protected abstract ElementMatcher<? super TypeDescription> getTypeMatcher();
    protected abstract Map<ElementMatcher, String> getTransformers();

    public AgentBuilder instrument(final AgentBuilder parent) {
        AgentBuilder agentBuilder = runSpecificTransformers(parent);
        return agentBuilder;
    }

    private AgentBuilder runSpecificTransformers(
            AgentBuilder builder
    ) {
        try {
            for (
                final Map.Entry<? extends ElementMatcher, String> entry :
                getTransformers().entrySet()
            ) {
                builder = builder.type(getTypeMatcher())
                    .transform(
                        new AgentBuilder.Transformer.ForAdvice()
                            .include(EpsagonRequestHandler.class.getClassLoader())
                            .advice(entry.getKey(), entry.getValue())
                    );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("unable to update classloader");
        }

        return builder;
    }

}
