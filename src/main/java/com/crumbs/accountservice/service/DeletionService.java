package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.entity.UserDetails;
import com.crumbs.accountservice.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(rollbackFor = { Exception.class })
public class DeletionService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    DeletionService(UserDetailsRepository userDetailsRepository,
                    PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void deleteCustomer(CustomerDeleteCredentials cred) {
        UserDetails user = userDetailsRepository.findByUsername(cred.getUsername()).orElseThrow(NoSuchElementException::new);
        if (!passwordEncoder.matches(cred.getPassword(), user.getPassword())) {
            throw new NoSuchElementException();
        }

        userDetailsRepository.delete(user);
    }
}
