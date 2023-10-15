package com.bezkoder.spring.login.Metamask;
import com.bezkoder.spring.login.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

public class Web3Authentication extends AbstractAuthenticationToken {
    private final String address;
    private final String signature;


    public Web3Authentication(String address, String signature) {
        super(null);
        this.address = address;
        this.signature = signature;
    }


    @Bean
    public AuthenticationManager auth(UserDetailsServiceImpl users) {
        return new ProviderManager(new Web3AuthenticationProvider(users));
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

