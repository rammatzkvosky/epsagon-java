package com.epsagon.executors;

import com.amazonaws.services.lambda.runtime.Context;
import java.lang.reflect.Method;

public class POJOExecutor extends BasePOJOExecutor {
    public POJOExecutor(Class<?> userHandlerClass, String handlerName) throws ExecutorException {
        super(userHandlerClass);
        Method candidate = null;
        for (Method m : _userHandlerClass.getMethods()) {
            if (m.getName().equals(handlerName)) {
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (
                    (candidate == null) ||
                    (m.getParameterCount() > candidate.getParameterCount()) ||
                    (
                        m.getParameterCount() == candidate.getParameterCount() &&
                            parameterTypes.length > 0 &&
                            parameterTypes[0] != Object.class && // can't be object, has to be JSON serializable.
                            parameterTypes[parameterTypes.length - 1] == Context.class
                    )
                ) {
                    System.out.println("Switching candidate: " + candidate + " with m: " + m);
                    candidate = m;
                }
            }
        }

        if (candidate == null) {
            throw new ExecutorException(
                    "No appropriate function with the name " +
                            handlerName +
                            " was found in " +
                            userHandlerClass.getCanonicalName()
            );
        }
        _userHandlerMethod = candidate;
    }
}