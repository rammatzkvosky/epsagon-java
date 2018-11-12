package com.epsagon.events.triggers;

import com.epsagon.events.EventBuildHelper;
import com.epsagon.protocol.EventOuterClass;

public class BaseTrigger {
    public static EventOuterClass.Event.Builder newBuilder() {
        return EventBuildHelper.newBuilder()
                .setOrigin("trigger")
                .setDuration(0); // Being explicit.
    }
}
