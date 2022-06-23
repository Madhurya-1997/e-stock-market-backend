package com.heritage.auth.services;

import com.heritage.auth.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    public Page<User> getAllUsers(Pageable pageable);
    public User register(String traceID, User user);
    public User update(String traceID, String userId, User userRequest);
}
