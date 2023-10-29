package com.bezkoder.spring.login.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  @Query("{username:?0}")
  User findByUsername(String username);

  @Query("{address:?0}")
  User findByAddress(String address);
  Boolean existsByUsername(String username);

}
