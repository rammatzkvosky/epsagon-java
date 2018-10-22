package com.epsagon.events.runners;

import com.amazonaws.services.lambda.runtime.Context;
import com.epsagon.ColdStart;
import com.epsagon.Region;
import com.epsagon.protocol.EventOuterClass;

public class BaseLambdaRunner {
    public static EventOuterClass.Event.Builder newBuilder(Context context) {
        EventOuterClass.Event.Builder builder = Runner.newBuilder();
        builder.setId(context.getAwsRequestId());
        builder.getResourceBuilder()
                .setOperation("invoke")
                .setName(context.getFunctionName())
                .putMetadata("log_stream_name", context.getLogStreamName())
                .putMetadata("log_group_name", context.getLogGroupName())
                .putMetadata("function_version", context.getFunctionVersion())
                .putMetadata("memory", String.valueOf(context.getMemoryLimitInMB()))
                .putMetadata("cold_start", String.valueOf(ColdStart.readAndSwitch()))
                .putMetadata("region", Region.getRegion())
                .putMetadata("aws_account", context.getInvokedFunctionArn().split(":")[4]);
        return builder;
    }
}
