package com.crumbs.accountservice.service;

import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.entity.UserStatus;
import com.crumbs.lib.repository.UserDetailsRepository;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

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

    public Page<UserDetails> getUsers(PageRequest pageRequest, String filter){

        Map<String, Function<PageRequest, Page<UserDetails>>> filterMethods = Map.of(
                "customer", userDetailsRepository::findUserDetailsByCustomerIsNotNull,
                "admin", userDetailsRepository::findUserDetailsByAdminIsNotNull,
                "owner", userDetailsRepository::findUserDetailsByOwnerIsNotNull,
                "driver", userDetailsRepository::findUserDetailsByDriverIsNotNull
        );
        
        if (filterMethods.containsKey(filter))
            return filterMethods.get(filter).apply(pageRequest);

        return userDetailsRepository.findAll(pageRequest);
    }

    public PageRequest getPageRequest(Integer page, Integer size, Map<String, String> extras){
        String sortBy = extras.get("sortBy");
        String orderBy = extras.get("orderBy");
        Sort.Direction direction = "asc".equals(orderBy) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
