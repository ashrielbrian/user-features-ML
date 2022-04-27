package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.model.UserFeature;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("testData")
public class TestFeatureSwitchDataService implements FeatureSwitchDao {

    private static List<UserFeature> userFeaturesDB = new ArrayList<>(Arrays.asList(
            new UserFeature(
                    "brian@tang.com",
                    new HashMap<>(Map.of("tipping", true, "cryptoTransfer", false))
            ),
            new UserFeature(
                    "another@test.com",
                    new HashMap<>(Map.of("tipping", true, "cryptoTransfer", true))
            )
    ));

    public List<UserFeature> getAllUsers() {
        return userFeaturesDB;
    }

    public Optional<UserFeature> selectUserByEmail(String email) {
        return userFeaturesDB.stream()
                .filter(user -> user.email().equals(email))
                .findFirst();
    }

    public boolean setFeatureFlag(String email, String featureName, boolean flag) {
        return selectUserByEmail(email).map(user -> {
            if (user.isEnabled(featureName) == flag) {
                // flag already set - do nothing
                return false;
            }
            user.addFeature(featureName, flag);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean getFeatureStatus(String email, String featureName) {
        return selectUserByEmail(email).map(user -> {
            return user.isEnabled(featureName);
        }).orElse(false);
    }
}
