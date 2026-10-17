 package com.appplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appplication.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    Optional<Users> findByUsername(String username);
}