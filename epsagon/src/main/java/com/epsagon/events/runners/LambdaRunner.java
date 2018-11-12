package com.epsagon.events.runners;

import com.amazonaws.services.lambda.runtime.Context;
import com.epsagon.protocol.EventOuterClass;

/**
 * A builder for a regular Lambda runner event.
 */
public class LambdaRunner {

    /**
     * Creates a new builder for a regular Lambda runner event.
     * @param context The context the Lambda was executed with.
     * @return A builder for the event.
     */
    public static EventOuterClass.Event.Builder newBuilder(Context context) {
        EventOuterClass.Event.Builder builder = BaseLambdaRunner.newBuilder(context);
        builder.getResourceBuilder().setType("lambda");
        return builder;
    }
}
