package com.epsagon.events.triggers;

import com.epsagon.events.EventBuildHelper;
import com.epsagon.protocol.EventOuterClass;

/**
 * A Factory for trigger event builders
 */
public class BaseTrigger {
    /**
     * @return An event builder for a trigger event.
     */
    public static EventOuterClass.Event.Builder newBuilder() {
        return EventBuildHelper.newBuilder()
                .setOrigin("trigger")
                .setDuration(0); // Being explicit.
    }
}
