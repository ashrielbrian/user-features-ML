package com.moneylion.featureswitch;

import com.moneylion.featureswitch.dao.FeatureSwitchDataService;
import com.moneylion.featureswitch.model.UserFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FeatureSwitchApplication.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FeatureSwitchDataServiceTest {
    @Autowired
    FeatureSwitchDataService dataService;

    @Test
    public void selectUserByEmailTest() {
        String email = "brian@tang.com";
        String notFoundEmail = "random@xyz.com";

        // returns UserFeature for an existing user
        UserFeature userFeature = dataService.selectUserByEmail(email).get();
        Assertions.assertEquals(userFeature.getEmail(), email);
        Assertions.assertTrue(userFeature.hasFeature("tipping"));
        Assertions.assertTrue(userFeature.isEnabled("tipping"));

        // assert returns null if user does not exist
        Assertions.assertTrue(dataService.selectUserByEmail(notFoundEmail)
                .isEmpty());
    }

    @Test
    public void getFeatureStatusTest() throws Exception{
        // test@user.com has cryptoTransfer feature disabled in V2__InsertTestData.sql
        String email = "test@user.com";
        String featureName = "cryptoTransfer";
        Boolean expectedFlag = false;

        Assertions.assertEquals(dataService.getFeatureStatus(email, featureName), expectedFlag);

        // test@user.com has tipping feature enabled in V2__InsertTestData.sql
        String enabledFeature = "tipping";

        // checks it is indeed enabled, based on the inserted test data
        Assertions.assertEquals(dataService.getFeatureStatus(email, enabledFeature), true);
    }

    @Test
    public void setFeatureFlagTest() throws Exception {
        // test_user@gmail.com has trade feature disabled in V2__InsertTestData.sql
        String email = "test_user@gmail.com";
        String featureName = "trade";
        Boolean flag = true;

        // successful modification of feature flag returns true
        Assertions.assertEquals(dataService.setFeatureFlag(email, featureName, flag), true);

        // check it has been updated on the database
        Assertions.assertEquals(dataService.getFeatureStatus(email, featureName), flag);

        // if a flag has already been modified, checks return false
        Assertions.assertEquals(dataService.setFeatureFlag(email, featureName, flag), false);
    }

}
