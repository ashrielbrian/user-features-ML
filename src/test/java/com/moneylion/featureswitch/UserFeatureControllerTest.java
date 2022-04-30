package com.moneylion.featureswitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.moneylion.featureswitch.api.UserFeatureController;
import com.moneylion.featureswitch.dao.FeatureSwitchDataService;
import com.moneylion.featureswitch.exceptions.FeatureNotFoundException;
import com.moneylion.featureswitch.exceptions.UserNotFoundException;
import com.moneylion.featureswitch.model.FeaturePostBody;
import com.moneylion.featureswitch.model.UserFeature;
import com.moneylion.featureswitch.service.UserFeatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserFeatureControllerTest {

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    private final UserFeature mockedUser_One = new UserFeature(
            "brian@tang.com",
            new HashMap<>(Map.of("tipping", true, "cryptoTransfer", false))
    );

    @Mock
    private FeatureSwitchDataService featureSwitchDataService;

    @Mock
    private UserFeatureService userFeatureService;

    @InjectMocks
    private UserFeatureController userFeatureController;

    @BeforeEach
    public void setUp() {
        // uses Mockito's MVC instead of a Tomcat server
        this.mockMvc = MockMvcBuilders.standaloneSetup(userFeatureController).build();
    }

    @Test
    public void getUserFeatureTest() throws Exception {
        String email = mockedUser_One.getEmail();
        String featureName = "tipping";
        Mockito.when(userFeatureService.getFeatureStatus(email, featureName))
                .thenReturn(true);

        mockMvc.perform(
                    MockMvcRequestBuilders.get("/feature")
                            .param("email", email)
                            .param("featureName", featureName)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.canAccess").value(true));
    }

    @Test
    public void getUserFeatureTest_UserThrowException() throws Exception {
        String email = mockedUser_One.getEmail();
        String featureName = "tipping";
        String exceptionMsg = "user not found.";

        Mockito.when(userFeatureService.getFeatureStatus(email, featureName))
                .thenThrow(new UserNotFoundException(exceptionMsg));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/feature")
                            .param("email", email)
                            .param("featureName", featureName)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(exceptionMsg));
    }

    @Test
    public void getUserFeatureTest_FeatureThrowException() throws Exception {
        String email = mockedUser_One.getEmail();
        String featureName = "tipping";
        String exceptionMsg = "feature not found.";

        Mockito.when(userFeatureService.getFeatureStatus(email, featureName))
                .thenThrow(new FeatureNotFoundException(exceptionMsg));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/feature")
                            .param("email", email)
                            .param("featureName", featureName)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(exceptionMsg));
    }

    @Test
    public void toggleUserFeatureTest() throws Exception {
        String email = mockedUser_One.getEmail();
        String featureName = "cryptoTransfer";
        Boolean flag = true;

        FeaturePostBody featureBody = new FeaturePostBody(featureName, email, flag);
        Mockito.when(userFeatureService.setFeatureFlag(
                    ArgumentMatchers.any(FeaturePostBody.class))
                )
                .thenReturn(true);

        String content = objectWriter.writeValueAsString(featureBody);

        // tests feature modified
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/feature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        // tests feature not modified
        Mockito.when(userFeatureService.setFeatureFlag(
                        ArgumentMatchers.any(FeaturePostBody.class))
                )
                .thenReturn(false);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotModified())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void toggleUserFeatureTest_UserThrowException() throws Exception {
        String email = mockedUser_One.getEmail();
        String featureName = "tipping";
        Boolean flag = true;
        String userExceptionMsg = "user not found.";

        FeaturePostBody featureBody = new FeaturePostBody(featureName, email, flag);
        Mockito.when(userFeatureService.setFeatureFlag(
                        ArgumentMatchers.any(FeaturePostBody.class))
                )
                .thenThrow(new UserNotFoundException(userExceptionMsg));

        String content = objectWriter.writeValueAsString(featureBody);

        // tests user not found
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/feature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(userExceptionMsg));

        String featureExceptionMsg = "feature not found.";

        // tests feature not found
        Mockito.when(userFeatureService.setFeatureFlag(
                        ArgumentMatchers.any(FeaturePostBody.class))
                )
                .thenThrow(new FeatureNotFoundException(featureExceptionMsg));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(featureExceptionMsg));
    }




}
