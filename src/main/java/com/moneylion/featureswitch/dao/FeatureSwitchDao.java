package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.model.UserFeature;

import java.util.List;
import java.util.Optional;

public interface FeatureSwitchDao {
    public Optional<UserFeature> selectUserByEmail(String email);
    public void setFeatureFlag(String email, String featureName, boolean flag);
    public boolean getFeatureStatus(String email, String featureName);
    public List<UserFeature> getAllUsers();
}
