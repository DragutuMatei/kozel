package com.bezkoder.spring.login.controllers;

import javax.servlet.http.Cookie;
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
//        origins = "https://fastalaneapp.netlify.app/",
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
    public ResponseEntity<?> challenge(@PathVariable String username, @PathVariable String address) {

//        User user = new User(username);
//        userRepository.save(user);
        User user = userRepository.findByAddress(address);

        System.out.println(username);

        if (user != null && user.getUsername().equals(username)) {
            System.out.println(user.getUsername());
            return ResponseEntity.ok(user.getNonce());
        } else {
            user = new User(username, address);
            userRepository.save(user);
            return ResponseEntity.ok(user.getNonce());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("=====================");
        System.out.println(loginRequest.getAddress());
        System.out.println(loginRequest.getSignature());
        Authentication authentication = manager.authenticate(new Web3Authentication(loginRequest.getAddress(), loginRequest.getSignature()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication.getName());
        System.out.println(authentication.isAuthenticated());


        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(loginRequest.getUsername());
        Cookie cookie = new Cookie(jwtCookie.getName(), jwtCookie.getValue());
        cookie.setPath(jwtCookie.getPath());

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        System.out.println(principal.toString());
//
//        if (principal instanceof UserDetails) {
//            UserDetails user = (UserDetails) principal;
//
//            Optional<User> response = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
//            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response.get());
//
//        }
//        return ResponseEntity.ok().body("oke");
        User user = userRepository.findByUsername(loginRequest.getUsername());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(user);


        //        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//        manager.authenticate(new Web3Authentication(loginRequest.getAddress(), loginRequest.getSignature()));
//
//        if (new Web3Authentication(loginRequest.getAddress(), loginRequest.getSignature()).isAuthenticated())
//        {
//            System.out.println("============================================OK============================");
//        }
//
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(new User(
//                        userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail(),
//                        userDetails.getAddress())
//                );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.ok(new MessageResponse("Error: Username is already taken!"));
        }

        User user = new User(signUpRequest.getUsername());
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