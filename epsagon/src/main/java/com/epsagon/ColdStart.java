package com.epsagon;

public class ColdStart {
    private static boolean _isColdStart = true;

    public static boolean read() {
        return _isColdStart;
    }

    public static void switchOff() {
        _isColdStart = false;
    }

    public static boolean readAndSwitch() {
        try {
            return read();
        } finally {
            switchOff();
        }
    }
}
