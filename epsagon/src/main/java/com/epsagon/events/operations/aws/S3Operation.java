package com.epsagon.events.operations.aws;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.services.s3.model.*;
import com.epsagon.protocol.EventOuterClass;

public class S3Operation {
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
