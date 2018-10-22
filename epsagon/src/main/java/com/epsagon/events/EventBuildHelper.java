package com.epsagon.events;

import com.epsagon.TimeHelper;
import com.epsagon.protocol.ErrorCodeOuterClass;
import com.epsagon.protocol.EventOuterClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class EventBuildHelper {
    public static EventOuterClass.Event.Builder newBuilder() {
        return EventOuterClass.Event.newBuilder()
                .setErrorCode(ErrorCodeOuterClass.ErrorCode.OK) // May be overridden
                .setStartTime(TimeHelper.getCurrentTime()); // May be overridden
    }

    public static EventOuterClass.Event.Builder newBuilder(Exception e) {
        EventOuterClass.Event.Builder builder = EventBuildHelper.newBuilder();
        return EventBuildHelper.setException(builder, e);
    }

    public static EventOuterClass.Event.Builder setDuration(
            EventOuterClass.Event.Builder eventBuilder
    ) {
        double startTime = eventBuilder.getStartTime();
        return eventBuilder.setDuration(TimeHelper.calcDuration(startTime));
    }

    public static EventOuterClass.Event.Builder setDuration(
            EventOuterClass.Event.Builder eventBuilder,
            long endTimeMillis
    ) {
        double startTime = eventBuilder.getStartTime();
        return eventBuilder.setDuration(TimeHelper.calcDuration(startTime, endTimeMillis));
    }

    public static EventOuterClass.Event.Builder setDuration(
            EventOuterClass.Event.Builder eventBuilder,
            double endTime
    ) {
        double startTime = eventBuilder.getStartTime();
        return eventBuilder.setDuration(TimeHelper.calcDuration(startTime, endTime));
    }

    public static EventOuterClass.Event.Builder setException(
            EventOuterClass.Event.Builder eventBuilder,
            Throwable e
    ) {
        eventBuilder.setErrorCode(ErrorCodeOuterClass.ErrorCode.EXCEPTION);
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        eventBuilder.getExceptionBuilder()
                .setType(e.getClass().getCanonicalName())
                .setMessage(Optional.ofNullable(e.getMessage()).orElse(""))
                .setTime(TimeHelper.getCurrentTime())
                .setTraceback(stackTrace.toString());

        return eventBuilder;
    }
}
