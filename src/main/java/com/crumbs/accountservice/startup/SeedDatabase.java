
package com.crumbs.accountservice.startup;

import com.crumbs.lib.entity.*;
import com.crumbs.lib.repository.*;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SeedDatabase{

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

    public void run(){
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

        UserDetails user;
        Admin admin;
        Customer customer;
        Driver driver;
        Owner owner;

        UserStatus status = userStatusRepository.getById("REGISTERED");
        DriverState state = driverStateRepository.getById("UNVALIDATED");
        DriverState avail = driverStateRepository.getById("AVAILABLE");

        DriverState state2 = driverStateRepository.getById("CHECKED_OUT");
        RestaurantStatus resStatus = restaurantStatusRepository.findById("ACTIVE").get();
        OrderStatus orderStatus = orderStatusRepository.getById("AWAITING_DRIVER");

        //Create Users
        user = UserDetails.builder().firstName("Elijah").lastName("Brooks")
                .username("user1").password(passwordEncoder.encode("123456")).email("1@1.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        user.setCustomer(customer);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("Jim").lastName("Brower")
                .username("user2").password(passwordEncoder.encode("123456")).email("2@2.com").phone("1234567890").build();
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state2).build();
        user.setDriver(driver);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("Admin").lastName("User")
                .username("user3").password(passwordEncoder.encode("123456")).email("3@3.com").phone("1234567890").build();
        admin = Admin.builder().userDetails(user).userStatus(status).build();
        user.setAdmin(admin);
        userDetailsRepository.save(user);

        user = UserDetails.builder().firstName("Jonathan").lastName("Frey")
                .username("user4").password(passwordEncoder.encode("123456")).email("4@4.com").phone("1234567890").build();
        owner = Owner.builder().userDetails(user).userStatus(status).build();
        user.setOwner(owner);
        user = userDetailsRepository.save(user);

        //Create Restaurant Location
        Location location1 = Location.builder()
                .state("TX")
                .street("10330 Norvic St")
                .city("Houston")
                .build();

        Location location2 = Location.builder()
                .state("TX")
                .street("715 Minnesota St")
                .city("Houston")
                .build();

        Location location3 = Location.builder()
                .state("TX")
                .street("5401 Caroline St")
                .city("Houston")
                .build();

        Location location4 = Location.builder()
                .state("TX")
                .street("8401 Concho St")
                .city("South Houston")
                .build();

        Location location5 = Location.builder()
                .state("TX")
                .street("8401 Sharpcrest St")
                .city("South Houston")
                .build();

        location1 = locationRepository.save(location1);
        location2 = locationRepository.save(location2);
        location3 = locationRepository.save(location3);
        location4 = locationRepository.save(location4);
        location5 = locationRepository.save(location5);

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantOwner(user.getOwner())
                .location(location1)
                .priceRating(1)
                .rating(5)
                .name("KFC")
                .restaurantStatus(resStatus)
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .restaurantOwner(user.getOwner())
                .location(location2)
                .priceRating(2)
                .rating(3)
                .name("MCDonald's")
                .restaurantStatus(resStatus)
                .build();

        Restaurant restaurant3 = Restaurant.builder()
                .restaurantOwner(user.getOwner())
                .location(location3)
                .priceRating(2)
                .rating(3)
                .name("Subway")
                .restaurantStatus(resStatus)
                .build();

        Restaurant restaurant4 = Restaurant.builder()
                .restaurantOwner(user.getOwner())
                .location(location4)
                .priceRating(4)
                .rating(1)
                .name("Cheesecake Factory")
                .restaurantStatus(resStatus)
                .build();

        Restaurant restaurant5 = Restaurant.builder()
                .restaurantOwner(user.getOwner())
                .location(location5)
                .priceRating(2)
                .rating(5)
                .name("Mario's Pizza")
                .restaurantStatus(resStatus)
                .build();

        restaurant1 = restaurantRepository.save(restaurant1);
        restaurant2 = restaurantRepository.save(restaurant2);
        restaurant3 = restaurantRepository.save(restaurant3);
        restaurant4 = restaurantRepository.save(restaurant4);
        restaurant5 = restaurantRepository.save(restaurant5);

        for (int i = 0; i < 10; i++){
            BigDecimal bd = BigDecimal.valueOf((i + 1F) * (float) Math.random() + 3)
                    .setScale(2, RoundingMode.HALF_UP);
            Float price = bd.floatValue();

            MenuItem menuItem = MenuItem.builder()
                    .name("MenuItem-"+i)
                    .price(price)
                    .description("Menu Item for a restaurant")
                    .build();

            menuItem.setRestaurant(restaurant1);
            menuItemRepository.save(menuItem);
            menuItem.setRestaurant(restaurant2);
            menuItemRepository.save(menuItem);
            menuItem.setRestaurant(restaurant3);
            menuItemRepository.save(menuItem);
            menuItem.setRestaurant(restaurant4);
            menuItemRepository.save(menuItem);
            menuItem.setRestaurant(restaurant5);
            menuItemRepository.save(menuItem);
        }
    }
}

