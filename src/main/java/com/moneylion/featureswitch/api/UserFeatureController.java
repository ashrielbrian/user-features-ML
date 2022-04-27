package com.moneylion.featureswitch.api;

import com.moneylion.featureswitch.service.UserFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/feature")
@RestController
public class UserFeatureController {
    private final UserFeatureService userFeatureService;

    @Autowired
    public UserFeatureController(UserFeatureService userFeatureService) {
        this.userFeatureService = userFeatureService;
    }

    @GetMapping
    public Map<String, Boolean> getUserFeatures(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "featureName") String featureName
    ) {
        boolean canAccess = userFeatureService.getFeatureStatus(email, featureName);
        return Map.of("canAccess", canAccess);
    }

}
