# Epsagon Tracer for Java
This package provides a tracer for Java code running on functions for collection of distributed
tracing and performence monitoring.

# Where to get packages
* For [Maven](https://maven.apache.org) projects, use:
```xml
<dependency>
  <groupId>com.epsagon</groupId>
  <artifactId>epsagon</artifactId>
  <version>1.1.0</version>
</dependency>
```

# Getting started
## Quick start
The easiest way to get started is as following:
* set the entry point to your Lambdas as `com.epsagon.EpsagonRequestHandler`
* set the following environment variables:
    * `EPSAGON_ENTRY_POINT` - The real entry point to your code, the one you had
                              previously configured (should be something like
                              `com.yourcompany.YourHandler::handlerMethod`)
    * `EPSAGON_TOKEN` - Epsagon's token, can be found at the 
                        [Dashboard](https://dashboard.epsagon.com)
    * `EPSAGON_APP_NAME` - A name for the application of this function, optional.

And that's it! Your function is ready for invocation.

## Alternative (No Environment Variables)
If you do not with to configure environment variables, please use this alternative:

First, create a simple class that extends `com.epsagon.EpsagonRequestHandler` like so:
```java
import com.epsagon.EpsagonRequestHandler;

public class EpsagonWrapper extends EpsagonRequestHandler {
    static {
        try {
            init("com.yourcompany.YourHandler::yourHandlerMethod")
                    .setToken("<your token>")
                    .setappName("<your application name>");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```
The class should have a static initializer that calls the `EpsagonRequestHandler.init()` method,
and gives it your Lambda's actual entry point as a parameter. The return value of this method
is an `EpsagonConfiguration` object. Configure your token and application name using this object,
like the example shows.

# Copyright
Provided under the MIT license. See LICENSE for details.

Copyright 2018, Epsagon
