package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.exceptions.FeatureNotFoundException;
import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.UserFeature;

import java.util.Optional;

public interface FeatureSwitchDao {
    /* Returns the user and all their features and flags */
    public Optional<UserFeature> selectUserByEmail(String email);
    /* Return true if successfully modified. Otherwise, return false. */
    public Boolean setFeatureFlag(String email, String featureName, Boolean flag) throws UserNotFoundException, FeatureNotFoundException;
    /* Returns whether a specific feature for a given user is enabled */
    public Boolean getFeatureStatus(String email, String featureName) throws UserNotFoundException, FeatureNotFoundException;
}
