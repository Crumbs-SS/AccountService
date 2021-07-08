package com.crumbs.accountservice.service;

import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserService(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public UserDetails userById(int userId) {
        return userDetailsRepository.findById((long) userId).orElseThrow();
    }

    public Long ownerExists(String username){
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow();
        if(user.getOwner()!= null)
            return user.getId();
        else
            throw new NoSuchElementException();
    }
}
