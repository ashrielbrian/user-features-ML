package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.UserFeature;

import java.util.List;
import java.util.Optional;

public interface FeatureSwitchDao {
    public Optional<UserFeature> selectUserByEmail(String email);
    /* Return true if successfully modified. Otherwise, return false. */
    public boolean setFeatureFlag(String email, String featureName, boolean flag) throws UserNotFoundException;
    public boolean getFeatureStatus(String email, String featureName) throws UserNotFoundException;
    public List<UserFeature> getAllUsers();
}
