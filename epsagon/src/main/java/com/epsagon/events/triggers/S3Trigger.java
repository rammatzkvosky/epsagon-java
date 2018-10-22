package com.epsagon.events.triggers;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.epsagon.protocol.EventOuterClass;


public class S3Trigger {
    public static EventOuterClass.Event.Builder newBuilder(
            S3Event event
    ) {
        S3EventNotification.S3EventNotificationRecord first = event.getRecords().get(0);
        EventOuterClass.Event.Builder builder = BaseTrigger.newBuilder()
                .setId("s3-trigger-" + first.getResponseElements().getxAmzRequestId());
        builder.getResourceBuilder()
                .setName(first.getS3().getBucket().getName())
                .setOperation(first.getEventName())
                .setType("s3")
                .putMetadata("region", first.getAwsRegion())
                .putMetadata("request_parameters", first.getRequestParameters().toString())
                .putMetadata("user_identity", first.getUserIdentity().toString())
                .putMetadata("object_key", first.getS3().getObject().getKey())
                .putMetadata("object_size", first.getS3().getObject().getSizeAsLong().toString())
                .putMetadata("object_etag", first.getS3().getObject().geteTag())
                .putMetadata("object_sequencer", first.getS3().getObject().getSequencer())
                .putMetadata("x-amz-request-id", first.getResponseElements().getxAmzRequestId());
        return builder;
    }
}
