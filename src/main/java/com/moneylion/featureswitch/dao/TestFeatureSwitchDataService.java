package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.model.UserFeature;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("testData")
public class TestFeatureSwitchDataService implements FeatureSwitchDao {

    private static List<UserFeature> userFeaturesDB = new ArrayList<>(Arrays.asList(
            new UserFeature(
                    "brian@tang.com",
                    Map.of("tipping", true, "cryptoTransfer", false)
            ),
            new UserFeature(
                    "another@test.com",
                    Map.of("tipping", true, "cryptoTransfer", true)
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

    public void setFeatureFlag(String email, String featureName, boolean flag) {
        selectUserByEmail(email).map(user -> {
            user.addFeature(featureName, flag);
            return 1;
        }).orElse(0);
    }

    @Override
    public boolean getFeatureStatus(String email, String featureName) {
        return selectUserByEmail(email).map(user -> {
            return user.isEnabled(featureName);
        }).orElse(false);
    }
}
