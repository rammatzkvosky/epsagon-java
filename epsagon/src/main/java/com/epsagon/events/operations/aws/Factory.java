package com.epsagon.events.operations.aws;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.handlers.HandlerContextKey;
import com.epsagon.protocol.EventOuterClass;

public class Factory {
    private final static Map<String, OperationBuilderInterface> OPERATIONS_BY_EVENT = (
        new HashMap<>()
    );

    static {
        OPERATIONS_BY_EVENT.put(
                "s3", S3Operation::newBuilder
        );
    }

    @FunctionalInterface
    public interface OperationBuilderInterface {
        EventOuterClass.Event.Builder newBuilder(
                Request<?> request,
                Response<?> response,
                Exception e
        );
    }

    public static EventOuterClass.Event.Builder newBuilder(
            Request<?> request,
            Response<?> response,
            Exception error
    ) {
        return OPERATIONS_BY_EVENT.getOrDefault(
                request.getHandlerContext(HandlerContextKey.SERVICE_ID).toLowerCase(),
                (req, res, e) -> null
        ).newBuilder(request, response, error);
    }

    public static EventOuterClass.Event.Builder newBuilder(
            Request<?> request,
            Response<?> response
    ) {
        return Factory.newBuilder(request, response, null);
    }

}
