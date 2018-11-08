package com.epsagon;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.epsagon.events.EventBuildHelper;
import com.epsagon.events.runners.LambdaRunner;
import com.epsagon.events.triggers.TriggerFactory;
import com.epsagon.protocol.EventOuterClass;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * This is the base class used to create Lambdas wrapped with Epsagon.
 * Set this class as the entry class to your program or extend it
 * with a static initializer that calls {@link #init(String)}}
 */
public class EpsagonRequestHandler implements RequestHandler<Object, Object> {
    private static final Logger _LOG = LogManager.getLogger(EpsagonRequestHandler.class);
    private static final ObjectMapper _objectMapper = new ObjectMapper()
              .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
              .registerModule(new JodaModule());
    private static final Trace _trace = Trace.getInstance();

    private static Class<?> _userHandlerClass;
    static {
        String epsagonEntryPoint = System.getenv("EPSAGON_ENTRY_POINT");
        if (epsagonEntryPoint != null) {
            try {
                init(epsagonEntryPoint);
            } catch (ClassNotFoundException e) {
                _LOG.error("Could not find class: " + epsagonEntryPoint + ". Please validate the path.");
            }
        }

    }

    public static EpsagonConfig init(String wrappedClass) throws ClassNotFoundException {
        Installer.install();
        _userHandlerClass = EpsagonRequestHandler.class.getClassLoader().loadClass(wrappedClass);
        return EpsagonConfig.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object handleRequest(Object input, Context context) {
        _trace.reset();
        EventOuterClass.Event.Builder runnerBuilder = LambdaRunner.newBuilder(context);
        runnerBuilder.setStartTime(TimeHelper.getCurrentTime());
        Constructor<?> ctor;
        RequestHandler<Object, Object> userRequestHandler = null;
        try {
            ctor = _userHandlerClass.getConstructor();
            userRequestHandler = (RequestHandler<Object, Object>) ctor.newInstance();
        } catch (Exception e) {
            _LOG.debug("An error Occurred instancing the client Class");
        }

        Type inputType = null;
        Type[] interfaces = userRequestHandler.getClass().getGenericInterfaces();
        for (Type genericInterface : interfaces) {
            // TODO make sure its the right interface
            if (genericInterface instanceof ParameterizedType) {
                inputType = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                break;
            }
        }

        if (inputType == null || !(inputType instanceof Class<?>)) {
            _LOG.debug("No appropriate interface was found");
        }

        // Not trying and catching here. If malformed input was given we should explode.
        // TODO: make trace still be sent here. make 2 levels of try / catch.
        // Should work on anything BUT S3Event, they have a bug (no empty constructor)
        Object realInput = _objectMapper.convertValue(input, (Class<?>) inputType);

        try {
            _trace.addEvent(
                    TriggerFactory.newBuilder(realInput, context)
            );
        } catch (Exception e) {
            _trace.addException(e);
        }

        try {
            return userRequestHandler.handleRequest(realInput, context);
        } catch (Throwable e) {
            EventBuildHelper.setException(runnerBuilder, e);
            throw e;
        } finally {
            _trace.addEvent(EventBuildHelper.setDuration(runnerBuilder));
            _trace.send();
        }
    }
}
