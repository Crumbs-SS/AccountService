package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.accountservice.dto.JPQLFilter;
import com.crumbs.lib.entity.Driver;
import com.crumbs.lib.entity.DriverRating;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.DriverRatingRepository;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.OrderRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


@Service
@Transactional(rollbackFor = { Exception.class })
public class UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DriverRatingRepository driverRatingRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public UserService(UserDetailsRepository userDetailsRepository,
                       DriverRepository driverRepository,
                       OrderRepository orderRepository,
                       DriverRatingRepository driverRatingRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.driverRepository = driverRepository;
        this.orderRepository = orderRepository;
        this.driverRatingRepository = driverRatingRepository;
    }

    public UserDetails userById(int userId) {
        return userDetailsRepository.findById((long) userId).orElseThrow();
    }

    public UserDetails userByUsername(String username) {
        return userDetailsRepository.findByUsername(username).orElseThrow();
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
    public String getDriverStatus(String username){
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Driver driver = user.getDriver();
        if (null == driver) { throw new EntityNotFoundException(); }
        return driver.getState().getState();
    }
    public Float getDriverPay(String username) {
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Driver driver = user.getDriver();
        if (null == driver) { throw new EntityNotFoundException(); }
        return driver.getTotalPay();
    }
    public Double getDriverAverageRating(String username){
        List<DriverRating> driverRatings = getDriverRatings(username);
       return driverRatings.stream().mapToInt(driverRating -> driverRating.getRating()).average().orElse(-1);
    }
    public List<DriverRating> getDriverRatings(String username){
        UserDetails user = userDetailsRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Driver driver = Optional.of(user.getDriver()).orElseThrow(EntityNotFoundException::new);
        return driverRatingRepository.findDriverRatingByDriverId(driver.getId());
    }
    public PageRequest getPageRequest(Integer page, Integer pageSize, String sortField, String sortDirection){
        if (sortField.equals("username")) {
            sortField = "userDetails.username";
        }
        Sort by = (sortDirection.equals("asc")) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return sortField!=null ? PageRequest.of(page, pageSize, by) : PageRequest.of(page, pageSize);
    }

    public Page<DriverDTO> getDrivers(PageRequest pageRequest, String searchString, String status){
        try{
            Page<Driver> drivers;
            if (searchString.isEmpty() && status.isEmpty()) {
                // neither string is used
                drivers = driverRepository.findAll(pageRequest);
            }
            else if (!searchString.isEmpty() && !status.isEmpty()) {
                // both strings are used
                drivers = driverRepository.findAllByStatusAndSearchString(status, searchString, pageRequest);
            }
            else if (!searchString.isEmpty()){
                // only searchString is used
                drivers = driverRepository.findAllBySearchString(searchString, pageRequest);
            }
            else {
                // only status is used
                drivers = driverRepository.findAllByStatus(status, pageRequest);
            }
            return drivers.map(this::driverEntityToDTO);

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Object checkIfDriverIsAvailable(String username){
        UserDetails userDetails = userDetailsRepository.findByUsername(username).orElseThrow();
        Driver driver = userDetails.getDriver();

        if (driver == null) return false;

        boolean doesDriverHaveAnOrder = orderRepository.findDriverAcceptedOrder(driver.getId()).size() > 0;
        boolean isDriverAvailable = "AVAILABLE".equals(driver.getState().getState());

        return isDriverAvailable && !doesDriverHaveAnOrder ? driver.getId() : false;
    }

    private DriverDTO driverEntityToDTO(Driver d) {
        DriverDTO dto = new DriverDTO();
        dto.setId(d.getId());
        dto.setFirstName(d.getUserDetails().getFirstName());
        dto.setLastName(d.getUserDetails().getLastName());
        dto.setUsername(d.getUserDetails().getUsername());
        dto.setEmail(d.getUserDetails().getEmail());
        dto.setPhone(d.getUserDetails().getPhone());
        dto.setLicenseId(d.getLicenseId());
        dto.setState(d.getState().getState());
        dto.setUserState(d.getUserStatus().getStatus());
        dto.setUserID(d.getUserDetails().getId());
        return dto;
    }
}
