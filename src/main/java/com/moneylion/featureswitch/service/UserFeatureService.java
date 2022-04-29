package com.moneylion.featureswitch.service;

import com.moneylion.featureswitch.dao.FeatureSwitchDao;
import com.moneylion.featureswitch.exceptions.FeatureNotFoundException;
import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.FeaturePostBody;
import com.moneylion.featureswitch.model.UserFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFeatureService {
    private final FeatureSwitchDao featureSwitchDao;

    @Autowired
    public UserFeatureService(@Qualifier("postgres") FeatureSwitchDao featureSwitchDao) {
        this.featureSwitchDao = featureSwitchDao;
    }

    public Boolean getFeatureStatus(String email, String featureName) throws UserNotFoundException, FeatureNotFoundException {
        return featureSwitchDao.getFeatureStatus(email, featureName);
    }

    public Boolean setFeatureFlag(FeaturePostBody featureBody) throws UserNotFoundException, FeatureNotFoundException {
        return featureSwitchDao.setFeatureFlag(
                featureBody.email(),
                featureBody.featureName(),
                featureBody.enable()
        );
    }




}
