package com.crumbs.accountservice.repository;

import com.crumbs.accountservice.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
    public Optional<UserDetails> findByUsernameOrEmail(String username, String email);
}