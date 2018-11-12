package com.epsagon;

import java.time.Instant;

/**
 * Helper classes for time conversions.
 */
public class TimeHelper {
    /**
     * Creates a double timestamp from milli seconds since epoch
     * @param millis milliseconds since epoch, UTC.
     * @return double timestamp, UTC.
     */
    public static double fromMillis(long millis) {
        return millis / 1000.0;
    }

    /**
     * @return The number of milliseconds since epoch, UTC.
     */
    public static long getCurrentMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * @return current time as a double.
     */
    public static double getCurrentTime() {
        return fromMillis(getCurrentMillis());
    }

    /**
     * calculates duration of an event.
     * @param startTime The time the event started.
     * @return The time difference between now and when the event started.
     */
    public static double calcDuration(double startTime) {
        return getCurrentTime() - startTime;
    }

    /**
     * calculates duration of an event.
     * @param startTime The time the event started.
     * @param endTime The time the event ended
     * @return The time difference between when the event ended and when the event started.
     */
    public static double calcDuration(double startTime, double endTime) {
        return endTime - startTime;
    }

    /**
     * calculates duration of an event.
     * @param startTime The time the event started.
     * @param endTimeMillis The time the event ended, in milliseconds since epoch.
     * @return The time difference between when the event ended and when the event started.
     */
    public static double calcDuration(double startTime, long endTimeMillis) {
        return calcDuration(startTime, fromMillis(endTimeMillis));
    }
}
