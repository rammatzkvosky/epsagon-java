package com.epsagon.events.runners;

import com.amazonaws.services.lambda.runtime.Context;
import com.epsagon.protocol.EventOuterClass;

public class LambdaRunner {

    public static EventOuterClass.Event.Builder newBuilder(Context context) {
        EventOuterClass.Event.Builder builder = BaseLambdaRunner.newBuilder(context);
        builder.getResourceBuilder().setType("lambda");
        return builder;
    }
}
