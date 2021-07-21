package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.JPQLFilter;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    public Page<UserDetails> getUsers(String query, PageRequest pageRequest, String filter){

        Map<String, Function<JPQLFilter, Page<UserDetails>>> filterMethods = Map.of(
                "customer", (param) -> userDetailsRepository
                        .findByCustomer(param.getPageRequest(), param.getQuery()),
                "admin",(param) -> userDetailsRepository
                        .findByAdmin(param.getPageRequest(), param.getQuery()),
                "owner", (param) -> userDetailsRepository
                        .findByOwner(param.getPageRequest(), param.getQuery()),
                "driver", (param) -> userDetailsRepository
                        .findByDriver(param.getPageRequest(), param.getQuery())
        );

        if (filterMethods.containsKey(filter)) {
            return filterMethods.get(filter).apply(JPQLFilter.builder()
                    .pageRequest(pageRequest).query(query.toLowerCase()).build());
        }

        return userDetailsRepository.findAll(getExample(query), pageRequest);
    }

    public PageRequest getPageRequest(Integer page, Integer size, Map<String, String> extras){
        String sortBy = extras.get("sortBy").toLowerCase();
        String orderBy = extras.get("orderBy").toLowerCase();
        Sort.Direction direction = "asc".equals(orderBy) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    private Example<UserDetails> getExample(String query){

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("phone");

        UserDetails user = UserDetails.builder()
                .email(query).lastName(query).username(query).firstName(query).build();
        return Example.of(user, exampleMatcher);
    }
}
