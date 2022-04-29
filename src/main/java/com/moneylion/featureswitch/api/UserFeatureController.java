package com.moneylion.featureswitch.api;

import com.moneylion.featureswitch.exceptions.FeatureNotFoundException;
import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.FeaturePostBody;
import com.moneylion.featureswitch.service.UserFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<?> getUserFeatures(
            @NonNull @RequestParam(name = "email") String email,
            @NonNull @RequestParam(name = "featureName") String featureName
    ) {
        try {
            boolean canAccess = userFeatureService.getFeatureStatus(email, featureName);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("canAccess", canAccess));
        } catch (UserNotFoundException | FeatureNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> toggleUserFeature(@Valid @NonNull @RequestBody FeaturePostBody featureBody) {
        try {
            boolean modified = userFeatureService.setFeatureFlag(featureBody);
            if (modified) {
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
            }
        } catch (UserNotFoundException | FeatureNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

}
