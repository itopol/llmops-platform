package com.llmops.platform.rag.api;

import com.llmops.platform.security.OrgAccessGuard;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AskController {

    private final OrgAccessGuard orgAccessGuard;

    public AskController(OrgAccessGuard orgAccessGuard) {
        this.orgAccessGuard = orgAccessGuard;
    }

    @PostMapping(value = "/{orgId}/ask", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AskResponse ask(
            @PathVariable String orgId,
            @Valid @RequestBody AskRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        orgAccessGuard.assertOrgAccess(jwt, orgId);
        return new AskResponse("RAG endpoint placeholder");
    }
}
