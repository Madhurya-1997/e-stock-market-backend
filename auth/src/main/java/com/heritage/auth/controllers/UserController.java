package com.heritage.auth.controllers;

import com.heritage.auth.models.User;
import com.heritage.auth.services.UserServiceImpl;
import com.heritage.auth.util.AuthResponse;
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
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        return new ResponseEntity<User>(userService.register(user), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return new ResponseEntity<Page<User>>(userService.getAllUsers(pageable), HttpStatus.OK);
    }



    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "userId") String userId,
                                           @RequestBody @Valid User userRequest) {
        return new ResponseEntity<User>(userService.update(userId, userRequest), HttpStatus.OK);
    }

}
