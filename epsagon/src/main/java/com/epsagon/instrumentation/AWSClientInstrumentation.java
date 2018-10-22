package com.epsagon.instrumentation;

import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.handlers.RequestHandler2;
import com.epsagon.Trace;
import com.epsagon.events.operations.aws.Factory;
import com.epsagon.protocol.EventOuterClass;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AWSClientInstrumentation extends EpsagonInstrumentation {

    @Override
    public ElementMatcher<TypeDescription> getTypeMatcher() {
        return ElementMatchers.named("com.amazonaws.AmazonWebServiceClient")
                .and(ElementMatchers.declaresField(ElementMatchers.named("requestHandler2s")));
    }

    @Override
    public Map<ElementMatcher, String> getTransformers() {
        final Map<ElementMatcher, String> transformers = new HashMap<>();
        transformers.put(ElementMatchers.isConstructor(), AWSClientAdvice.class.getName());
        return transformers;
    }

    public static class AWSClientAdvice {
        @Advice.OnMethodExit(suppress = Throwable.class)
        public static void addEpsagonHandler(
            @Advice.FieldValue("requestHandler2s") final List<RequestHandler2> handlers
        ) {
            for (final RequestHandler2 handler : handlers) {
                if (handler instanceof EpsagonAWSRequestHandler) {
                    return;
                }
            }
            try {
                handlers.add(new AWSClientInstrumentation.EpsagonAWSRequestHandler());
            } catch (Throwable e) {
                System.out.println("Error in adding handler");
                System.out.println(e.getCause());
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static class EpsagonAWSRequestHandler extends RequestHandler2 {
        /**
        * {@inheritDoc}
        */
        private Trace _trace = Trace.getInstance();

        @Override
        public void beforeRequest(Request<?> request) {
            // TODO: if problem of async event occures, add "promise" here
            // TODO: should be done via the get/setHandlerContext methods.
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public void afterResponse(Request<?> request, Response<?> response) {
            try {
                _trace.addEvent(
                        Factory.newBuilder(
                                request,
                                response
                        )
                );
            } catch (Exception error) {
                _trace.addException(error);
            }
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public void afterError(Request<?> request, Response<?> response, Exception e) {
            try {
                _trace.addEvent(
                        Factory.newBuilder(
                                request,
                                response,
                                e
                        )
                );
            } catch (Exception error) {
                _trace.addException(error);
            }
        }


    }

}
