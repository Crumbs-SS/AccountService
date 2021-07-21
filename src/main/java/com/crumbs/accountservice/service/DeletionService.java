package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.entity.UserStatus;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(rollbackFor = { Exception.class })
public class DeletionService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

    public UserDetails deleteUser(Long userId){
        String status = "DELETED";
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        UserStatus userStatus = UserStatus.builder()
                .status(status)
                .build();

        setStatusForAllRoles(userStatus, user);


        return userDetailsRepository.save(user);
    }


    private void setStatusForAllRoles(UserStatus userStatus, UserDetails user){
        if(user.getCustomer() != null)
            user.getCustomer().setUserStatus(userStatus);
        if(user.getOwner() != null)
            user.getOwner().setUserStatus(userStatus);
        if(user.getDriver() != null)
            user.getDriver().setUserStatus(userStatus);
        if(user.getAdmin() != null)
            user.getAdmin().setUserStatus(userStatus);
    }

}
