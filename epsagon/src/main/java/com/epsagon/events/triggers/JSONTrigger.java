package com.epsagon.events.triggers;

import com.amazonaws.services.lambda.runtime.Context;
import com.epsagon.Trace;
import com.epsagon.events.MetadataBuilder;
import com.epsagon.protocol.EventOuterClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.UUID;

/**
 * A builder factory for a JSON trigger.
 */
public class JSONTrigger {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param event The event the Lambda was triggered with.
     * @param context The context the Lambda was triggered with.
     * @return a builder for a JSON trigger.
     */
    public static EventOuterClass.Event.Builder newBuilder(
        Object event,
        Context context
    ) {
        EventOuterClass.Event.Builder builder = BaseTrigger.newBuilder()
                .setId("trigger-" + UUID.randomUUID().toString());
        HashMap<String, String> metadata = null;


        try {
            metadata = new MetadataBuilder(
                    builder.getResourceBuilder().getMetadataMap()
            ).putIfAllData("data", objectMapper.writeValueAsString(event)).build();
        } catch (JsonProcessingException e) {
            Trace.getInstance().addException(e);
        }

        builder.getResourceBuilder()
                .setName("trigger-" + context.getFunctionName())
                .setOperation("json")
                 .setType("json");
        if (metadata != null) {
            builder.getResourceBuilder().putAllMetadata(metadata);
        }
        return builder;


    }
}
