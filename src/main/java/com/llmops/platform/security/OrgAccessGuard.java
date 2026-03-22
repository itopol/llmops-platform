package com.llmops.platform.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class OrgAccessGuard {

    private final SecurityProperties securityProperties;

    public OrgAccessGuard(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public void assertOrgAccess(Jwt jwt, String orgId) {
        String claimName = securityProperties.getOrgClaimName();
        String claimValue = jwt.getClaimAsString(claimName);
        if (claimValue == null || !claimValue.equals(orgId)) {
            throw new AccessDeniedException("Token org claim does not match requested org.");
        }
    }
}
