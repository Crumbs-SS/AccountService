package com.crumbs.accountservice;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.lib.entity.UserDetails;

public class MockUtil {

    public static UserDetails getUser(){
        return UserDetails.builder()
                .id(-1L)
                .build();
    }

    public static CustomerDeleteCredentials getCred(){
        return CustomerDeleteCredentials.builder()
                .username("testguy12")
                .password("testguy12")
                .build();
    }

    public static UserDetailsUpdate getUserDetailsUpdate(){
        return UserDetailsUpdate.builder()
                .username("testguy12")
                .email("testguy12@gmail.com")
                .firstName("test")
                .lastName("guy")
                .phone("1231231234")
                .build();
    }

    public static EnableUser getEnableUser(){
        return EnableUser.builder()
                .admin(true)
                .build();
    }
}
