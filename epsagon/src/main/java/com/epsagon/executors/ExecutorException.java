package com.epsagon.executors;

import com.epsagon.EpsagonException;

/**
 * An exception that is raised when an {@link Executor} encounters an error.
 */
public class ExecutorException extends EpsagonException {
    public ExecutorException(String message) {
        super(message);
    }
}
