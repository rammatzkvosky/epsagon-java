package com.epsagon.events.triggers;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import com.epsagon.protocol.EventOuterClass;

public class TriggerFactory {
    private final static Map<Class<?>, TriggerNewBuilderInterface> TRIGGERS_BY_EVENT = new HashMap<>();
    static {
        TRIGGERS_BY_EVENT.put(
                S3Event.class, (event) -> S3Trigger.newBuilder((S3Event) event)
        );

    }

    @FunctionalInterface
    interface TriggerNewBuilderInterface {
        EventOuterClass.Event.Builder newBuilder(Object event);
    }

    public static EventOuterClass.Event.Builder newBuilder(
        Object event,
        Context context
    ) {
        return TRIGGERS_BY_EVENT.getOrDefault(
            event.getClass(),
            (e) -> JSONTrigger.newBuilder(e, context)
        ).newBuilder(event);
    }
}
