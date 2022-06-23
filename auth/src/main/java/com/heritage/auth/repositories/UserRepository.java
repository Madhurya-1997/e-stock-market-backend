package com.heritage.auth.repositories;

import com.heritage.auth.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username:?0}")
    Optional<User> findUserByUsername(String username);

    @Query("{email:?0}")
    Optional<User> findUserByEmail(String email);
}
