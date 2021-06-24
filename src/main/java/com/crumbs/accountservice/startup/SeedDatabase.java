package com.crumbs.accountservice.startup;

import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SeedDatabase implements ApplicationRunner {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserStatusRepository userStatusRepository;
    private final DriverStateRepository driverStateRepository;
    //for owner demo
    private final RestaurantRepository restaurantRepository;
    private final RestaurantOwnerRepository ownerRepository;
    private final LocationRepository locationRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantStatusRepository restaurantStatusRepository;

    @Autowired
    SeedDatabase(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserDetailsRepository userDetailsRepository,
                 @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserStatusRepository userStatusRepository,
                 @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DriverStateRepository driverStateRepository,
                 RestaurantRepository restaurantRepository, RestaurantOwnerRepository ownerRepository,
                 LocationRepository locationRepository, MenuItemRepository menuItemRepository,
                 RestaurantCategoryRepository restaurantCategoryRepository, CategoryRepository categoryRepository,
                 RestaurantStatusRepository restaurantStatusRepository
                 , PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.userStatusRepository = userStatusRepository;
        this.driverStateRepository = driverStateRepository;
        this.passwordEncoder = passwordEncoder;

        this.restaurantRepository = restaurantRepository;
        this.ownerRepository = ownerRepository;
        this.locationRepository = locationRepository;
        this.menuItemRepository = menuItemRepository;
        this.restaurantCategoryRepository = restaurantCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantStatusRepository = restaurantStatusRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        menuItemRepository.deleteAll();
//        restaurantCategoryRepository.deleteAll();
//        restaurantRepository.deleteAll();
//        locationRepository.deleteAll();
//        ownerRepository.deleteAll();
//        userDetailsRepository.deleteAll();

        UserDetails user;
        Admin admin;
        Customer customer;
        Driver driver;
        Owner owner;

//        Restaurant restaurant;
//        Restaurant restaurant2;
//        UserDetails userDetail;
//        Owner restaurantOwner;
//        Location location;
//        Location location2;

        UserStatus status = userStatusRepository.getById("REGISTERED");
        DriverState state = driverStateRepository.getById("UNVALIDATED");


        user = UserDetails.builder().firstName("1").lastName("1")
                .username("user1").password(passwordEncoder.encode("123456")).email("1@1.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        user.setCustomer(customer);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("2").lastName("2")
                .username("user2").password(passwordEncoder.encode("123456")).email("2@2.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state).build();
        user.setCustomer(customer);
        user.setDriver(driver);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("3").lastName("3")
                .username("user3").password(passwordEncoder.encode("123456")).email("3@3.com").phone("1234567890").build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state).build();
        user.setDriver(driver);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;

        user = UserDetails.builder().firstName("4").lastName("4")
                .username("user4").password(passwordEncoder.encode("123456")).email("4@4.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        owner = Owner.builder().userDetails(user).userStatus(status).build();
        user.setCustomer(customer);
        user.setOwner(owner);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
//        // for owner restaurants demo, can delete later
//        location = Location.builder()
//                .state("CA")
//                .street("1111 Street A")
//                .city("Los Angeles")
//                .zipCode("12345")
//                .build();
//
//        location2 = Location.builder()
//                .state("CA")
//                .street("2222 Street B")
//                .city("Los Angeles")
//                .zipCode("12345")
//                .build();
//
//        RestaurantStatus resStatus = restaurantStatusRepository.findById("ACTIVE").get();
//
//        restaurant = Restaurant.builder()
//                .restaurantOwner(owner)
//                .location(location)
//                .priceRating(1)
//                .rating(5)
//                .name("KFC")
//                .restaurantStatus(resStatus)
//                .build();
//
//        restaurant2 = Restaurant.builder()
//                .restaurantOwner(owner)
//                .location(location2)
//                .priceRating(2)
//                .rating(3)
//                .name("MCDonald's")
//                .restaurantStatus(resStatus)
//                .build();
//
//        restaurant = restaurantRepository.save(restaurant);
//        restaurant2 = restaurantRepository.save(restaurant2);
//
//        for (int i = 0; i < 5; i++){
//            BigDecimal bd = BigDecimal.valueOf((i + 1F) * (float) Math.random() + 3)
//                    .setScale(2, RoundingMode.HALF_UP);
//            Float price = bd.floatValue();
//
//            MenuItem menuItem = MenuItem.builder()
//                    .name("MenuItem-"+i)
//                    .price(price)
//                    .description("Menu Item for a restaurant")
//                    .build();
//
//            menuItem.setRestaurant(restaurant);
//            menuItemRepository.save(menuItem);
//        }
//        for (int i = 0; i < 5; i++){
//            BigDecimal bd = BigDecimal.valueOf((i + 1F) * (float) Math.random() + 3)
//                    .setScale(2, RoundingMode.HALF_UP);
//            Float price = bd.floatValue();
//
//            MenuItem menuItem = MenuItem.builder()
//                    .name("MenuItem-"+i)
//                    .price(price)
//                    .description("Menu Item for a restaurant")
//                    .build();
//
//            menuItem.setRestaurant(restaurant2);
//            menuItemRepository.save(menuItem);
//        }
//        //end of restaurant



        user = null;

        user = UserDetails.builder().firstName("jim").lastName("brower")
                .username("jbrower").password(passwordEncoder.encode("123456")).email("jim@brower.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state).build();
        user.setCustomer(customer);
        user.setDriver(driver);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            userDetailsRepository.save(user);
        }
        user = null;
    }
}
