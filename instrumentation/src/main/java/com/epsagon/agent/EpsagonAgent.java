package com.epsagon.agent;


import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The agent class, which enables us to perform instrumentation. Bundled as a different jar
 * and injected to the running process.
 */
public class EpsagonAgent {
    private static volatile Instrumentation _instrumentation;

    /**
     * The main method of the agent. Loads and executes the Patcher.
     * @param args The arguments to the agent.
     * @param instrumentation The instrumentation object to use.
     */
    public static void agentmain(String args, Instrumentation instrumentation){
        try {
            _instrumentation = instrumentation;

            URL[] news = {
                new File("/var/task/").toURI().toURL()
            };

            URLClassLoader newClassLoader = new URLClassLoader(news, null);
            Thread.currentThread().setContextClassLoader(newClassLoader);
            final Class<?> patcher = newClassLoader.loadClass("com.epsagon.Patcher");
            final Method instrumentAll = patcher.getMethods()[0];
            instrumentAll.invoke(null, instrumentation);
        } catch (Exception e) {
            System.out.println("Error in agent");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }

    }
}
