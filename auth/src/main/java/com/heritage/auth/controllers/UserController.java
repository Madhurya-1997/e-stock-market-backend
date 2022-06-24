package com.heritage.auth.controllers;

import com.heritage.auth.models.User;
import com.heritage.auth.services.UserServiceImpl;
import com.heritage.auth.util.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
public class UserController {


    @Autowired
    private UserServiceImpl userService;


    /**
     * Registration of a user
     */
    @Operation(summary = "Register as a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Registration Successful",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid User Request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestHeader("e-stock-market-trace-id") String traceID,
                                         @RequestBody @Valid User user) {
        return new ResponseEntity<User>(userService.register(traceID, user), HttpStatus.OK);
    }

    @Operation(summary = "Get all users from DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched all users successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return new ResponseEntity<Page<User>>(userService.getAllUsers(pageable), HttpStatus.OK);
    }


    @Operation(summary = "To update user based on UserID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User is updated Successful",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid User Request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Resource not available",
                    content = @Content)
    })
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@RequestHeader("e-stock-market-trace-id") String traceID,
                                           @PathVariable(value = "userId") String userId,
                                           @RequestBody @Valid User userRequest) {
        return new ResponseEntity<User>(userService.update(traceID, userId, userRequest), HttpStatus.OK);
    }

}
