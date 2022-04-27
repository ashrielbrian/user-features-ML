package com.moneylion.featureswitch.model;

import java.util.Map;

public class UserFeature {
    private final String email;
    private final Map<String, Boolean> features;

    public UserFeature(String email, Map<String, Boolean> features) {
        this.email = email;
        this.features = features;
    }

    public String email() {
        return this.email;
    }

    public Map<String, Boolean> features() {
        return this.features;
    }

    public boolean isEnabled(String feature) {
        return this.features.get(feature);
    }

    public void addFeature(String featureName, Boolean flag) {
        features.put(featureName, flag);
    }
}
