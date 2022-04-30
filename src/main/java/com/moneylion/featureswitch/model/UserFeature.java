package com.moneylion.featureswitch.model;

import java.util.Map;
import java.util.Optional;

public class UserFeature {
    private final String email;
    private Map<String, Boolean> features;

    public UserFeature(String email, Map<String, Boolean> features) {
        this.email = email;
        this.features = features;
    }

    public String getEmail() {
        return this.email;
    }

    public Map<String, Boolean> getFeatures() {
        return this.features;
    }

    public Boolean isEnabled(String feature) {
        return this.features.get(feature);
    }

    public void addFeature(String featureName, Boolean flag) {
        features.put(featureName, flag);
    }

    public Boolean hasFeature(String featureName) {
        Optional<Boolean> inFeatureSet = Optional.ofNullable(features.get(featureName));
        return inFeatureSet.isPresent();
    }
}
