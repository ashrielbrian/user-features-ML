package com.moneylion.featureswitch.dao;

import com.moneylion.featureswitch.exceptions.FeatureNotFoundException;
import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.UserFeature;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("postgres")
public class PostgresFeatureSwitchDao implements FeatureSwitchDao{
    private final JdbcTemplate jdbcTemplate;

    
    @Override
    public Optional<UserFeature> selectUserByEmail(String email) {
        String query = "SELECT id FROM user where email = ?";

        // get the user's id
        return Optional.empty();
    }

    @Override
    public boolean setFeatureFlag(String email, String featureName, boolean flag) throws UserNotFoundException {
        String query = "SELECT email, featureName FROM test";

        Optional<UserFeature> userFeature = selectUserByEmail(email);
        return userFeature.map(user -> {
            return user.isEnabled(featureName);
        }).orElseThrow(() -> new UserNotFoundException("No such user."));
    }

    @Override
    public boolean getFeatureStatus(String email, String featureName) throws UserNotFoundException {
        String query = """
            SELECT enabled
            FROM user_feature uf
            JOIN features f
            ON f.
        """;
        return false;
    }

    @Override
    public List<UserFeature> getAllUsers() {
        String query = "SELECT id, email FROM users;";
        return List<UserFeature>();
    }
}
