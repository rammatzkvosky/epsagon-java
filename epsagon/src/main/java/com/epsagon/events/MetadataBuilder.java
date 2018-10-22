package com.epsagon.events;

import com.epsagon.EpsagonConfig;

import java.util.HashMap;
import java.util.Map;

public class MetadataBuilder {
    private HashMap<String, String> _metadata;

    public MetadataBuilder(Map<String, String> existing) {
        _metadata = new HashMap<>(existing);
    }

    public MetadataBuilder() {
        _metadata = new HashMap<>();
    }

    public MetadataBuilder put(String key, String value) {
        _metadata.put(key, value);
        return this;
    }

    public MetadataBuilder putIfAllData(String key, String value) {
        if (!EpsagonConfig.getInstance().isMetadataOnly()) {
            _metadata.put(key, value);
        }
        return this;
    }

    public HashMap<String, String> build() {
        return _metadata;
    }
}
