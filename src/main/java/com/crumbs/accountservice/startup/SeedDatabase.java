
package com.crumbs.accountservice.startup;

import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
@Transactional
public class SeedDatabase implements ApplicationRunner {
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserStatusRepository userStatusRepository;
    private final DriverStateRepository driverStateRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantOwnerRepository ownerRepository;
    private final LocationRepository locationRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantStatusRepository restaurantStatusRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final CartItemRepository cartItemRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final DriverRatingRepository driverRatingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        UserDetails user;
//        Admin admin;
//        Customer customer;
//        Driver driver;
//        Owner owner;
//
//        UserStatus status = userStatusRepository.getById("REGISTERED");
//        DriverState state = driverStateRepository.getById("UNVALIDATED");
//        DriverState avail = driverStateRepository.getById("AVAILABLE");
//
//        DriverState state2 = driverStateRepository.getById("CHECKED_OUT");
//        RestaurantStatus resStatus = restaurantStatusRepository.findById("ACTIVE").get();
//        OrderStatus orderStatus = orderStatusRepository.getById("AWAITING_DRIVER");
//
//        //Create Users
//        user = UserDetails.builder().firstName("Elijah").lastName("Brooks")
//                .username("Customer").password(passwordEncoder.encode("123456")).email("1@1.com").phone("1234567890").build();
//        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
//        user.setCustomer(customer);
//        userDetailsRepository.save(user);
//
//        user = UserDetails.builder().firstName("Jim").lastName("Brower")
//                .username("Driver").password(passwordEncoder.encode("123456")).email("2@2.com").phone("1234567890").build();
//        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state2).build();
//        user.setDriver(driver);
//        userDetailsRepository.save(user);
//
//        user = UserDetails.builder().firstName("Admin").lastName("User")
//                .username("Admin").password(passwordEncoder.encode("123456")).email("3@3.com").phone("1234567890").build();
//        admin = Admin.builder().userDetails(user).userStatus(status).build();
//        user.setAdmin(admin);
//        userDetailsRepository.save(user);
//
//        user = UserDetails.builder().firstName("Jonathan").lastName("Frey")
//                .username("Owner").password(passwordEncoder.encode("123456")).email("4@4.com").phone("1234567890").build();
//        owner = Owner.builder().userDetails(user).userStatus(status).build();
//        user.setOwner(owner);
//        userDetailsRepository.save(user);

    }
}

