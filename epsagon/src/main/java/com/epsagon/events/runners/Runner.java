package com.epsagon.events.runners;

import com.epsagon.events.EventBuildHelper;
import com.epsagon.protocol.EventOuterClass;

public class Runner {
    public static EventOuterClass.Event.Builder newBuilder() {
        return EventBuildHelper.newBuilder()
                .setOrigin("runner");
    }
}
