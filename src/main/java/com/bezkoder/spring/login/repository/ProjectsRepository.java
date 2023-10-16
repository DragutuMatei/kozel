package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Projects;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends MongoRepository<Projects, String> {

    @Query("{user_id:?0}")
    List<Projects> findByUserId(String id);

}
