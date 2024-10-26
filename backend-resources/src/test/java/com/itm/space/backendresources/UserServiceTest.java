package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceTest extends BaseIntegrationTest {

    private final Keycloak keycloak;

    private String userId;

    @Autowired
    public UserServiceTest(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest("username", "email@test.com", "password",
                "First", "Last");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
        userId = keycloak.realm("ITM").users().search(userRequest.getUsername()).get(0).getId();
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testGetUserById() throws Exception {
        String id = "320cd3fe-0222-44aa-a57e-fc23c71c5cbd";
        mvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        if (userId != null) {
            keycloak.realm("ITM").users().get(userId).remove();
        }
    }
}