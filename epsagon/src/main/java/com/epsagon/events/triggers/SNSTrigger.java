package com.epsagon.events.triggers;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.epsagon.events.MetadataBuilder;
import com.epsagon.protocol.EventOuterClass;

/**
 * A builder factory for a SNS trigger.
 */
public class SNSTrigger {
    /**
     * @param event The event the Lambda was triggered with.
     * @return a builder for a SNS trigger.
     */
    public static EventOuterClass.Event.Builder newBuilder(
            SNSEvent event
    ) {
        SNSEvent.SNSRecord first = event.getRecords().get(0);
        EventOuterClass.Event.Builder builder = BaseTrigger.newBuilder()
                .setId(first.getSNS().getMessageId());
        MetadataBuilder metadataBuilder = new MetadataBuilder(builder.getResourceBuilder().getMetadataMap())
                .put("Notification Subject", first.getSNS().getSubject())
                .putIfAllData("Notification Message", first.getSNS().getMessage());
        builder.getResourceBuilder()
                .setName(first.getEventSubscriptionArn().split(":")[5])
                .setOperation(first.getSNS().getType())
                .setType("sns")
                .putAllMetadata(metadataBuilder.build());
        return builder;
    }

}
