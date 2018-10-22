package com.epsagon.agent;


import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class EpsagonAgent {
    private static volatile Instrumentation _instrumentation;

    public static void agentmain(String args, Instrumentation instrumentation){
        try {
            _instrumentation = instrumentation;

            URL[] news = {
                new File("/var/task/").toURI().toURL()
            };

            URLClassLoader ncl = new URLClassLoader(news, null);
            Thread.currentThread().setContextClassLoader(ncl);
            final Class<?> patcher = ncl.loadClass("com.epsagon.Patcher");
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
