package com.heritage.auth.services;


import com.heritage.auth.exceptions.ResourceNotFoundException;
import com.heritage.auth.exceptions.UserAlreadyExistsException;
import com.heritage.auth.models.User;
import com.heritage.auth.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Page<User> getAllUsers(Pageable pageable)  {
        return userRepository.findAll(pageable);
    }

    @Override
    public User register(String traceID, User user) {
        logger.debug("Invoking register service with trace ID: " + traceID);

        Optional<User> existingUserByUsername = userRepository.findUserByUsername(user.getUsername());
        Optional<User> existingUserByEmail = userRepository.findUserByEmail(user.getEmail());

        if (existingUserByUsername.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with same username!!");
        }
        if (existingUserByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with same email!!");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public User update(String traceID, String userId, User userRequest) {
        logger.debug("Invoking update service with trace ID: " + traceID);

        return userRepository.findById(userId).map(user -> {
            user.setUsername(userRequest.getUsername());
            user.setPassword(userRequest.getPassword());
            user.setEmail(userRequest.getEmail());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found !!"));
    }
}
