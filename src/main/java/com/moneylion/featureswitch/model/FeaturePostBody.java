package com.moneylion.featureswitch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FeaturePostBody {

    @NotBlank
    private final String featureName;
    @NotBlank
    private final String email;
    @NotNull
    private final Boolean enable;

    public FeaturePostBody(@JsonProperty("featureName") String featureName,
                           @JsonProperty("email") String email,
                           @JsonProperty("enable") Boolean enable) {
        this.featureName = featureName;
        this.email = email;
        this.enable = enable;
    }

    public String getFeatureName() {
        return featureName;
    }
    public String getEmail() {
        return email;
    }
    public Boolean getEnable() {
        return enable;
    }
}
