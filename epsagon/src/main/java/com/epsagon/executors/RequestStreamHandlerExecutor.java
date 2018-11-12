package com.epsagon.executors;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.InputStream;
import java.io.OutputStream;

public class RequestStreamHandlerExecutor extends Executor {
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

    public void execute(InputStream input, OutputStream output, Context context) throws Throwable {
       System.out.println("not implemented yet");
       _userHandlerMethod.invoke(_userHandlerObj, input, output, context);
    }
}
