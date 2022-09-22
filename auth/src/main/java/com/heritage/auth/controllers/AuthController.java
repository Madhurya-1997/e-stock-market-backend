package com.heritage.auth.controllers;

import com.heritage.auth.repositories.UserRepository;
import com.heritage.auth.security.JwtUtil;
import com.heritage.auth.services.MyUserDetailsService;
import com.heritage.auth.util.AuthRequest;
import com.heritage.auth.util.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @Operation(summary = "Display home page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Home page is working",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/")
    public String welcome() {
        return ("<h1>Welcome to Auth Service!!</h1>");
    }

    /**
     * Login as a user
     */
    @Operation(summary = "Login as a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Login Successful",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid Login Credentials",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestHeader("e-stock-market-trace-id") String traceID,
                                                                  @RequestBody AuthRequest req) throws Exception {

        logger.debug("Invoking createAuthenticationToken service with trace ID: " + traceID);

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

    @Operation(summary = "Validate the logged in User with JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Validation Successful",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid Token or Token Expired",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/validate")
    public AuthResponse verifyUser(@RequestHeader("e-stock-market-trace-id") String traceID,
                                   @RequestHeader("Authorization") String token) throws AccessDeniedException {

        logger.debug("Invoking verifyUser service with trace ID: " + traceID);

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
