package com.crumbs.accountservice.service;

import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserService(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public UserDetails userById(int userId) {
        return userDetailsRepository.findById(userId).orElseThrow();
    }
}
