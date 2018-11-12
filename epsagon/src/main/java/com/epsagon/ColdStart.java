package com.epsagon;

/**
 * Helper class for managing cold start state
 */
public class ColdStart {
    private static boolean _isColdStart = true;

    /**
     * Reads the cold start state
     * @return The cold start state
     */
    public static boolean read() {
        return _isColdStart;
    }

    /**
     * Swithces off the cold start flag
     */
    public static void switchOff() {
        _isColdStart = false;
    }

    /**
     * reads the cold start state, and switches it off
     * @return The cold start state.
     */
    public static boolean readAndSwitch() {
        try {
            return read();
        } finally {
            switchOff();
        }
    }
}
