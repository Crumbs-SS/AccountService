package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerUpdate;
import com.crumbs.accountservice.entity.UserDetails;
import com.crumbs.accountservice.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(rollbackFor = { Exception.class })
public class UpdateService {

    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    UpdateService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public UserDetails updateCustomer(CustomerUpdate cred) {
        UserDetails user = userDetailsRepository.findByUsername(cred.getUsername()).orElseThrow(NoSuchElementException::new);
        user.setUsername(cred.getUsername());
        user.setEmail(cred.getEmail());
        user.setFirstName(cred.getFirstName());
        user.setLastName(cred.getLastName());
        user = userDetailsRepository.save(user);
        return user;
    }
}
