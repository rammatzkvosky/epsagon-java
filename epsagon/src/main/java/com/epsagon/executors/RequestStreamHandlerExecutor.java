package com.epsagon.executors;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An executor for handlers implementing the
 * {@link com.amazonaws.services.lambda.runtime.RequestStreamHandler} interface.
 */
public class RequestStreamHandlerExecutor extends Executor {
    /**
     * @param userHandlerClass The class of the user handler.
     * @throws ExecutorException
     */
    public RequestStreamHandlerExecutor(Class<?> userHandlerClass) throws ExecutorException {
        super(userHandlerClass);
        try {
            _userHandlerMethod = _userHandlerClass.getMethod(
                    "handleRequest",
                    InputStream.class,
                    OutputStream.class,
                    Context.class
            );
        } catch (NoSuchMethodException e) {
            throw new ExecutorException(
                    "no handleRequest implementation found in " +
                            userHandlerClass.getCanonicalName()
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void execute(InputStream input, OutputStream output, Context context) throws Throwable {
       System.out.println("not implemented yet");
       _userHandlerMethod.invoke(_userHandlerObj, input, output, context);
    }
}
