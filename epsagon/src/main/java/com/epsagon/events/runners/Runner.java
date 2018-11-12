package com.epsagon.events.runners;

import com.epsagon.events.EventBuildHelper;
import com.epsagon.protocol.EventOuterClass;

/**
 * A builder factory for a runner event.
 */
public class Runner {
    /**
     * @return A builder for a runner event.
     */
    public static EventOuterClass.Event.Builder newBuilder() {
        return EventBuildHelper.newBuilder()
                .setOrigin("runner");
    }
}
