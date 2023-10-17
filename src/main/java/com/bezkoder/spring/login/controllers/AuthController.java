package com.bezkoder.spring.login.controllers;

import javax.validation.Valid;

import com.bezkoder.spring.login.Metamask.Web3Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.request.LoginRequest;
import com.bezkoder.spring.login.payload.request.SignupRequest;
import com.bezkoder.spring.login.payload.response.UserInfoResponse;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.jwt.JwtUtils;
import com.bezkoder.spring.login.security.services.UserDetailsImpl;

import java.util.Optional;

@CrossOrigin(
        allowCredentials = "true",
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    private final AuthenticationManager manager;

    public AuthController(AuthenticationManager manager) {
        this.manager = manager;
    }

    //iau nonce
    @GetMapping("/challenge/{username}/{address}")
    public String challenge(@PathVariable String username, @PathVariable String address) {
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setAddress(address);
            userRepository.save(user);
            return user.getNonce();
        }
        throw new UnknownAddress(username);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        System.out.println(manager.authenticate(new Web3Authentication(loginRequest.getAddress(), loginRequest.getSignature())));

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getNonce(),
                        userDetails.getAddress())
                );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.ok(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.ok(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/userInfo")
    public Optional<User> getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(principal.toString());

        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
//            return user;
            Optional<User> response = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));

            System.out.println(response.toString());
            return response;
        } else {
            String username = principal.toString();
            System.out.println(username);
            System.out.println("naspa");
            return null;
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnknownAddress extends RuntimeException {
    public UnknownAddress(String address) {
        super("Wallet address " + address + " is not known");
    }
}