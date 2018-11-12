package com.epsagon.events.operations.aws;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.services.s3.model.*;
import com.epsagon.protocol.EventOuterClass;

/**
 * A builder for an event describing an S3 operation
 */
public class S3Operation {
    /**
     * Creates a new Builder, with some fields pre-initialized.
     * @param request The AWS Request object.
     * @param response The AWS Response object, if any. (may be null)
     * @param e An exception for the request, if any. (may be null)
     * @return A builder with pre-initialized fields.
     */
    public static EventOuterClass.Event.Builder newBuilder(
            Request<?> request,
            Response<?> response,
            Exception e
    ) {
        EventOuterClass.Event.Builder builder = AWSSDKOperation.newBuilder(request, response, e);
        AmazonWebServiceRequest awsReq = request.getOriginalRequest();
        if (response != null) {
            AmazonWebServiceResponse awsResp = (AmazonWebServiceResponse) response.getAwsResponse();
            switch (builder.getResourceBuilder().getOperation()) {
                case "PutObject":
                    PutObjectRequest req = (PutObjectRequest) awsReq;
                    PutObjectResult res = (PutObjectResult) awsResp.getResult();
                    builder.getResourceBuilder()
                            .setName(req.getBucketName())
                            .putMetadata("key", req.getKey());
                    if (res != null) {
                        builder.getResourceBuilder()
                                .putMetadata("etag", res.getETag());

                    }
            }
        }
        return builder;
    }
}
