package com.bezkoder.spring.login.Metamask;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.security.services.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;

@Component
public class Web3AuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl users;

    public Web3AuthenticationProvider(UserDetailsServiceImpl users) {
        this.users = users;
    }

    private boolean valid(String signature, String address, String nonce) throws SignatureException {
        String r = signature.substring(0, 66);
        String s = "0x" + signature.substring(66, 130);
        String v = "0x" + signature.substring(130, 132);

        Sign.SignatureData data = new Sign.SignatureData(
                Numeric.hexStringToByteArray(v),
                Numeric.hexStringToByteArray(r),
                Numeric.hexStringToByteArray(s)
        );

        BigInteger key = Sign.signedPrefixedMessageToKey(nonce.getBytes(), data);
        return matches(key, address);
    }

    private boolean matches(BigInteger key, String address) {
        return ("0x" + Keys.getAddress(key).toLowerCase()).equals(address.toLowerCase());
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        User user = users.findByAddress(authentication.getName());

        System.out.println("================1=================");
//        System.out.println(authentication.getName());
        System.out.print("Username: ");
        System.out.println(user.getUsername());

        if (user != null) {
            String signature = authentication.getCredentials().toString();
            System.out.print("signature: ");
            System.out.println(signature);
            try {
                if (valid(signature, user.getAddress(), user.getNonce())) {
                    System.out.println("merge");
                    return new Web3Authentication(user.getAddress(), signature);
                }else
                    System.out.println("nu merge");

            } catch (SignatureException e) {
                throw new BadCredentialsException(authentication.getName() + " is not allowed to log in.");

            }
        }
        throw new BadCredentialsException(authentication.getName() + " is not allowed to log in.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return Web3Authentication.class == authentication;
    }
}

