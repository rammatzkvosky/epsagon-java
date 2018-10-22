package com.epsagon;

import java.util.Optional;

public class EpsagonConfig {
    // Breaking the builder pattern for usability here
    private String _token = System.getenv("EPSAGON_TOKEN");
    private String _appName = System.getenv("EPSAGON_APP_NAME");
    private String _traceCollectorURL = Optional
            .ofNullable(System.getenv("EPSAGON_TRACE_COLLECTOR_URL"))
            .orElse("https://" + Region.getRegion() + ".tc.epsagon.com");
    private boolean _metadataOnly = false;

    private static EpsagonConfig _instance = new EpsagonConfig();

    public static EpsagonConfig getInstance() {
        return _instance;
    }

    public String getToken() {
        return _token;
    }

    public EpsagonConfig setToken(String token) {
        _token = token;
        return this;
    }

    public String getappName() {
        return _appName;
    }

    public EpsagonConfig setappName(String appName) {
        _appName = appName;
        return this;
    }

    public boolean isMetadataOnly() {
        return _metadataOnly;
    }

    public EpsagonConfig setMetadataOnly(boolean metadataOnly) {
        _metadataOnly = metadataOnly;
        return this;
    }

    public String getTraceCollectorURL() {
        return _traceCollectorURL;
    }

    public EpsagonConfig setTraceCollectorURL(String traceCollectorURL) {
        _traceCollectorURL = traceCollectorURL;
        return this;
    }

}
