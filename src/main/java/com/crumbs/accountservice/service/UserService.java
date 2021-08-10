package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.JPQLFilter;
import com.crumbs.lib.entity.Driver;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.function.Function;

import java.util.NoSuchElementException;


@Service
public class UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public UserService(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserDetailsRepository userDetailsRepository, DriverRepository driverRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.driverRepository = driverRepository;
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

    private Example<UserDetails> getExample(String query) {

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

    public Long ownerExists(String username){
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow();
        if(user.getOwner()!= null)
            return user.getId();
        else
            throw new NoSuchElementException();
    }
    public String getDriverStatus(Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return driver.getState().getState();
    }
    public Float getDriverPay(Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return driver.getTotalPay();
    }
}
