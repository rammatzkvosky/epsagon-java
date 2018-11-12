package com.epsagon.events;

import com.epsagon.TimeHelper;
import com.epsagon.protocol.ErrorCodeOuterClass;
import com.epsagon.protocol.EventOuterClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * A helper class for building events.
 */
public class EventBuildHelper {
    /**
     * @return An event builder with some default fields initialized.
     */
    public static EventOuterClass.Event.Builder newBuilder() {
        return EventOuterClass.Event.newBuilder()
                .setErrorCode(ErrorCodeOuterClass.ErrorCode.OK) // May be overridden
                .setStartTime(TimeHelper.getCurrentTime()); // May be overridden
    }

    /**
     * @param e An exception to set for the event
     * @return An event builder that is initialized with an exception.
     */
    public static EventOuterClass.Event.Builder newBuilder(Exception e) {
        EventOuterClass.Event.Builder builder = EventBuildHelper.newBuilder();
        return EventBuildHelper.setException(builder, e);
    }

    /**
     * Sets the duration of an event, using current time
     * @param eventBuilder The builder for the event to set the duration of.
     * @return The event builder.
     */
    public static EventOuterClass.Event.Builder setDuration(
            EventOuterClass.Event.Builder eventBuilder
    ) {
        double startTime = eventBuilder.getStartTime();
        return eventBuilder.setDuration(TimeHelper.calcDuration(startTime));
    }

    /**
     * Sets the duration of an event, using a timestamp.
     * @param eventBuilder The builder for the event to set the duration of.
     * @param endTimeMillis The time the event ended in, in milliseconds since epoch.
     * @return The event builder.
     */
    public static EventOuterClass.Event.Builder setDuration(
            EventOuterClass.Event.Builder eventBuilder,
            long endTimeMillis
    ) {
        double startTime = eventBuilder.getStartTime();
        return eventBuilder.setDuration(TimeHelper.calcDuration(startTime, endTimeMillis));
    }

    /**
     * Sets the duration of an event, using a timestamp.
     * @param eventBuilder The builder for the event to set the duration of.
     * @param endTime The time the event ended in.
     * @return The event builder.
     */
    public static EventOuterClass.Event.Builder setDuration(
            EventOuterClass.Event.Builder eventBuilder,
            double endTime
    ) {
        double startTime = eventBuilder.getStartTime();
        return eventBuilder.setDuration(TimeHelper.calcDuration(startTime, endTime));
    }

    /**
     * Sets the exception for an event
     * @param eventBuilder The builder for the event to set the exception of.
     * @param e The exception that was raised.
     * @return The event builder.
     */
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
