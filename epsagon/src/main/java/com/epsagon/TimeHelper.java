package com.epsagon;

import java.time.Instant;

public class TimeHelper {
    public static double fromMillis(long millis) {
        return millis / 1000.0;
    }
    public static long getCurrentMillis() {
        return Instant.now().toEpochMilli();
    }
    public static double getCurrentTime() {
        return fromMillis(getCurrentMillis());
    }

    public static double calcDuration(double startTime) {
        return getCurrentTime() - startTime;
    }

    public static double calcDuration(double startTime, double endTime) {
        return endTime - startTime;
    }

    public static double calcDuration(double startTime, long endTimeMillis) {
        return calcDuration(startTime, fromMillis(endTimeMillis));
    }
}
