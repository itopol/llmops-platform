package com.llmops.platform.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private String orgClaimName = "org_id";

    public String getOrgClaimName() {
        return orgClaimName;
    }

    public void setOrgClaimName(String orgClaimName) {
        this.orgClaimName = orgClaimName;
    }
}
