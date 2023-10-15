package com.bezkoder.spring.login.Metamask;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class Web3Authentication extends AbstractAuthenticationToken {
    private final String address;
    private final String signature;

    public Web3Authentication(String address, String signature) {
        super(null);
        this.address = address;
        this.signature = signature;
    }

    @Override
    public Object getPrincipal() {
        return address;
    }

    @Override
    public Object getCredentials() {
        return signature;
    }
}

