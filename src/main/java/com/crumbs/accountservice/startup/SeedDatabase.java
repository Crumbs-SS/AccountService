
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
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

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    SeedDatabase( UserDetailsRepository userDetailsRepository,
                  UserStatusRepository userStatusRepository,
                  DriverStateRepository driverStateRepository,
                 RestaurantRepository restaurantRepository, RestaurantOwnerRepository ownerRepository,
                 LocationRepository locationRepository, MenuItemRepository menuItemRepository,
                 RestaurantCategoryRepository restaurantCategoryRepository, CategoryRepository categoryRepository,
                 RestaurantStatusRepository restaurantStatusRepository, OrderRepository orderRepository,
                  OrderStatusRepository orderStatusRepository, CustomerRepository customerRepository,
                 DriverRepository driverRepository, FoodOrderRepository foodOrderRepository
                 , CartItemRepository cartItemRepository, ConfirmationTokenRepository confirmationTokenRepository,
                  PasswordEncoder passwordEncoder, DriverRatingRepository driverRatingRepository,
                  PaymentRepository paymentRepository) {
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
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.cartItemRepository = cartItemRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.driverRatingRepository = driverRatingRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        driverRatingRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
        cartItemRepository.deleteAll();
        foodOrderRepository.deleteAll();
        menuItemRepository.deleteAll();
        restaurantCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();
        locationRepository.deleteAll();
        ownerRepository.deleteAll();
        customerRepository.deleteAll();
        driverRepository.deleteAll();
        userDetailsRepository.deleteAll();
        orderRepository.deleteAll();
        paymentRepository.deleteAll();

        //Create Categories
        Category category = Category.builder().name("Burger").build();
        categoryRepository.save(category);
        category = Category.builder().name("Japanese").build();
        categoryRepository.save(category);
        category = Category.builder().name("American").build();
        categoryRepository.save(category);
        category = Category.builder().name("Sushi").build();
        categoryRepository.save(category);
        category = Category.builder().name("Tex-Mex").build();
        categoryRepository.save(category);
        category = Category.builder().name("BBQ").build();
        categoryRepository.save(category);
        category = Category.builder().name("Pizza").build();
        categoryRepository.save(category);
        category = Category.builder().name("Italian").build();
        categoryRepository.save(category);
        category = Category.builder().name("Fine-Dining").build();
        categoryRepository.save(category);
        category = Category.builder().name("Fast-Food").build();
        categoryRepository.save(category);


    }
}

