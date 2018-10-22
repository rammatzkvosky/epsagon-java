cd instrumentation/ &&
    mvn clean install &&
    cd .. &&
    cp instrumentation/target/instrumentation-1.0-SNAPSHOT.jar epsagon/src/main/resources/agent.jar &&
    sync &&
    cd epsagon/ && mvn clean install &&
    cd ..
