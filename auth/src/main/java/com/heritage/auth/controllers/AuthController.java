package com.heritage.auth.controllers;

import com.heritage.auth.exceptions.UserAlreadyExistsException;
import com.heritage.auth.exceptions.UserTokenExpiredException;
import com.heritage.auth.models.User;
import com.heritage.auth.repositories.UserRepository;
import com.heritage.auth.security.JwtUtil;
import com.heritage.auth.services.MyUserDetailsService;
import com.heritage.auth.services.UserServiceImpl;
import com.heritage.auth.util.AuthRequest;
import com.heritage.auth.util.AuthResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/")
    public String welcome() {
        return ("<h1>Welcome to Auth Service!!</h1>");
    }

    /**
     * Login as a user
     */
    @GetMapping("/authenticate")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest req) throws Exception {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getUsername(), req.getPassword()
            ));
        } catch(BadCredentialsException e) {
            throw new Exception("Incorrect username or password");
        }
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(req.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse( jwt,
                userRepository.findUserByUsername(userDetails.getUsername()).get().getUsername(),
                userRepository.findUserByUsername(userDetails.getUsername()).get().getEmail()));
    }

    @GetMapping("/validate")
    public AuthResponse verifyUser(@RequestHeader("Authorization") String token) throws AccessDeniedException {

        log.debug("Invoking validate controller...");
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (userDetails == null) {
            throw new AccessDeniedException("Access Denied for user: " + userDetails.getUsername());
        }
        return new AuthResponse(token,
                userDetails.getUsername(),
                userRepository.findUserByUsername(userDetails.getUsername()).get().getEmail());
    }

}
