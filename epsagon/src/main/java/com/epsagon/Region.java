package com.epsagon;

import java.util.Optional;

/**
 * A helper class for identifying our AWS region.
 */
public class Region {
    public static final String DEFAULT_REGION = "us-east-1";
    private static final String _region = Optional.ofNullable(
            System.getenv("AWS_REGION")
    ).orElse(DEFAULT_REGION);


    /**
     * @return The region the Lambda is executing in, or {@link Region#DEFAULT_REGION} as default.
     */
    public static String getRegion() {
        return _region;
    }
}
