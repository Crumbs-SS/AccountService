package com.crumbs.accountservice;

import com.crumbs.accountservice.dto.CustomerDeleteCredentials;
import com.crumbs.accountservice.dto.EnableUser;
import com.crumbs.accountservice.dto.UserDetailsUpdate;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Optional;

public class MockUtil {

    public static UserDetails getUser(){
        return UserDetails.builder()
                .username("testguy")
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
                .customer(false)
                .driver(false)
                .owner(false)
                .build();
    }

    public static PageRequest getPageRequest(){
        return PageRequest.of(1, 5, Sort.by(Sort.Direction.ASC, "id"));
    }

    public static Page<UserDetails> getPageUsers(){
        return Page.empty(getPageRequest());
    }

    public static Map<String, String> getExtras(){
        return Map.of("sortBy", "id", "orderBy", "asc");
    }

    public static Optional<UserDetails> getOptionalUser(){
        return Optional.of(getUser());
    }
}
