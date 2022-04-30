package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.exceptions.FeatureNotFoundException;
import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.UserFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository("postgres")
public class FeatureSwitchDataService implements FeatureSwitchDao{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FeatureSwitchDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserFeature> selectUserByEmail(String email) {
        final String query = """
            WITH usr AS (
                SELECT uf.user_id, uf.enabled, uf.feature_id
                FROM user_feature AS uf
                WHERE uf.user_id = (
                    SELECT id FROM users u
                    WHERE u.email = ?
                )
            )
            SELECT usr.*, f.name as feature_name
            FROM usr
            JOIN features f
            ON f.id = usr.feature_id;
        """;
        Map<String, Boolean> features = new HashMap<>();
        jdbcTemplate.query(query, (resultSet) -> {
            features.put(
                    resultSet.getString("feature_name"),
                    resultSet.getBoolean("enabled")
            );
        }, email);

        if (features.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new UserFeature(email, features));
    }

    @Override
    public Boolean setFeatureFlag(
            String email,
            String featureName,
            boolean flag
    ) throws UserNotFoundException, FeatureNotFoundException {
        String query = """
            UPDATE user_feature 
            SET enabled = ? 
            WHERE user_id = (
                SELECT id FROM users
                WHERE email = ?
            )
            AND feature_id = (
                SELECT id FROM features
                WHERE name = ?
            )
        """;

        Optional<UserFeature> userFeature = selectUserByEmail(email);
        UserFeature user = userFeature.orElseThrow(() -> new UserNotFoundException("No such user."));

        throwIfFeatureNotFound(user, featureName);

        if (user.isEnabled(featureName) == flag) {
            // no update required to the feature flag
            return false;
        }

        jdbcTemplate.update(query, flag, email, featureName);
        return true;
    }

    @Override
    public Boolean getFeatureStatus(
            String email,
            String featureName
    ) throws UserNotFoundException, FeatureNotFoundException {
        Optional<UserFeature> userFeature = selectUserByEmail(email);

        UserFeature user = userFeature.orElseThrow(() -> new UserNotFoundException("No such user."));
        throwIfFeatureNotFound(user, featureName);

        return user.isEnabled(featureName);
    }

    private void throwIfFeatureNotFound(
            UserFeature user, String featureName
    ) throws FeatureNotFoundException {
        if (!user.hasFeature(featureName)) {
            throw new FeatureNotFoundException(
                    String.format("User %s has no such feature %s.", user.getEmail(), featureName)
            );
        }
    }
}
