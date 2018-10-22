package com.epsagon;

import java.util.Optional;

public class Region {
    private static final String _region = Optional.ofNullable(
            System.getenv("AWS_REGION")
    ).orElse("us-east-1");


    public static String getRegion() {
        return _region;
    }
}
