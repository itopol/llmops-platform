package com.llmops.platform.rag.api;

import com.llmops.platform.security.OrgAccessGuard;
import com.llmops.platform.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AskController.class)
@Import({SecurityConfig.class, OrgAccessGuard.class})
class AskControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void askWithoutTokenReturns401() throws Exception {
        mockMvc.perform(post("/org-a/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What is this?\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void askWithWrongOrgClaimReturns403() throws Exception {
        mockMvc.perform(post("/org-a/ask")
                        .with(jwt().jwt(jwt -> jwt.claim("org_id", "org-b")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What is this?\"}"))
                .andExpect(status().isForbidden());
    }
}
