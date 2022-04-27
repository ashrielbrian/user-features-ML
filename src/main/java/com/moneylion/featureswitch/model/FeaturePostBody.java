package com.moneylion.featureswitch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeaturePostBody {

    private final String featureName;
    private final String email;
    private final Boolean enable;

    public FeaturePostBody(@JsonProperty("featureName") String featureName,
                           @JsonProperty("email") String email,
                           @JsonProperty("enable") Boolean enable) {
        this.featureName = featureName;
        this.email = email;
        this.enable = enable;
    }

    public String featureName() {
        return featureName;
    }
    public String email() {
        return email;
    }
    public Boolean enable() {
        return enable;
    }
}
