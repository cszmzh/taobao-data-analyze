package com.banana.visual.repository;

import com.banana.visual.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(nativeQuery = true, value = "select count(*) from user")
    Long countUserNumber();
}
