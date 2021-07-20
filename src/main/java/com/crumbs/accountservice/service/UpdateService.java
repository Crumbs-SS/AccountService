package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.CustomerUpdate;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.accountservice.exception.EmailNotAvailableException;
import com.crumbs.accountservice.exception.UsernameNotAvailableException;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.entity.UserStatus;
import com.crumbs.lib.repository.UserDetailsRepository;
import jdk.jfr.Enabled;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(rollbackFor = { Exception.class })
public class UpdateService {

    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

    public UserDetails updateUser(Long userId, UserDetailsUpdate cred){
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();

        user.setFirstName(cred.getFirstName());
        user.setLastName(cred.getLastName());
        user.setPhone(cred.getPhone());

        boolean usernameTaken =  userDetailsRepository.findByUsername(cred.getUsername())
                .orElse(user).getId().equals(user.getId());
        boolean emailTaken = userDetailsRepository.findByEmail(cred.getEmail())
                .orElse(user).getId().equals(user.getId());

        if( !usernameTaken || !emailTaken ){
            throw !usernameTaken ? new UsernameNotAvailableException() : new EmailNotAvailableException();
        }

        user.setUsername(cred.getUsername());
        user.setEmail(cred.getEmail());

        user = userDetailsRepository.save(user);
        return user;
    }

    public UserDetails enableUser(Long userId, EnableUser enableUser){
        String status = "ACTIVE";
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        UserStatus userStatus = UserStatus.builder()
                .status(status)
                .build();

        setStatusForAllRoles(userStatus, enableUser, user);
        return userDetailsRepository.save(user);
    }

    private void setStatusForAllRoles(UserStatus userStatus, EnableUser enableUser, UserDetails user){
        if(enableUser.getCustomer() && user.getCustomer() != null)
            user.getCustomer().setUserStatus(userStatus);
        if(enableUser.getOwner() && user.getOwner() != null)
            user.getOwner().setUserStatus(userStatus);
        if(enableUser.getDriver() && user.getDriver() != null)
            user.getDriver().setUserStatus(userStatus);
        if(enableUser.getAdmin() && user.getAdmin() != null)
            user.getAdmin().setUserStatus(userStatus);
    }
}
