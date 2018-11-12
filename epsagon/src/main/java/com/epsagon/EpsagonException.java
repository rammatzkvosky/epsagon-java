package com.epsagon;

/**
 * A base class for all Epsagon exceptions.
 */
public class EpsagonException extends Exception {
    public EpsagonException(String errorMessage) {
        super(errorMessage);
    }
}
