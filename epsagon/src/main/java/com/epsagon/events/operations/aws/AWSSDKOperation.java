package com.epsagon.events.operations.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.handlers.HandlerContextKey;
import com.epsagon.TimeHelper;
import com.epsagon.events.EventBuildHelper;
import com.epsagon.protocol.EventOuterClass;

import java.util.Optional;

public class AWSSDKOperation {
    public static EventOuterClass.Event.Builder newBuilder(
            Request<?> request,
            Response<?> response,
            Exception e
    ) {
        EventOuterClass.Event.Builder builder = EventOuterClass.Event.newBuilder()
                .setOrigin("aws-sdk")
                .setStartTime(TimeHelper.fromMillis(
                        request.getAWSRequestMetrics().getTimingInfo().getStartEpochTimeMilliIfKnown()
                ));

        EventBuildHelper.setDuration(
                builder,
                Optional.ofNullable(
                    request.getAWSRequestMetrics().getTimingInfo().getEndEpochTimeMilliIfKnown()
                ).orElse(TimeHelper.getCurrentMillis())
        );

        builder.getResourceBuilder()
                .setOperation(request.getHandlerContext(HandlerContextKey.OPERATION_NAME))
                .setType(request.getHandlerContext(HandlerContextKey.SERVICE_ID).toLowerCase())
                .putMetadata("Region", request.getHandlerContext(HandlerContextKey.SIGNING_REGION));

        if (response != null) {
            AmazonWebServiceResponse awsResp = (AmazonWebServiceResponse) response.getAwsResponse();
            builder.setId(awsResp.getRequestId())
                    .getResourceBuilder()
                    .putMetadata("Status Code", String.valueOf(response.getHttpResponse().getStatusCode()));
                    // TODO Add retry attempts
        }

        if (e != null) {
            EventBuildHelper.setException(builder, e);
            if (e instanceof AmazonServiceException) {
                AmazonServiceException amazonErr = (AmazonServiceException) e;
                builder.setId(amazonErr.getRequestId()).getResourceBuilder()
                        .putMetadata("Status Code", String.valueOf(amazonErr.getStatusCode()));
                        // TODO Add retry attempts
            }
        }
        return builder;
    }
}
