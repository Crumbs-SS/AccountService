
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
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.foodOrderRepository = foodOrderRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
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
        //Create UserDetails, and roles

        UserDetails user;
        Admin admin;
        Customer customer;
        Driver driver;
        Owner owner;

        UserStatus status = userStatusRepository.getById("REGISTERED");
        DriverState state = driverStateRepository.getById("UNVALIDATED");
        DriverState state2 = driverStateRepository.getById("CHECKED_OUT");
        RestaurantStatus resStatus = restaurantStatusRepository.findById("ACTIVE").get();
        OrderStatus orderStatus = orderStatusRepository.getById("AWAITING_DRIVER");

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
        driver = Driver.builder().userDetails(user).licenseId("54321").userStatus(status).state(state2).build();
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
        owner = ownerRepository.save(owner);
//        customer = customerRepository.save(customer);
        user.setCustomer(customer);
        user.setOwner(owner);
        if (userDetailsRepository.findByUsername(user.getUsername()).isEmpty()) {
            user = userDetailsRepository.save(user);
        }

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


        //Create Restaurants
        Restaurant restaurant;
        Restaurant restaurant2;
        UserDetails userDetail;
        Owner restaurantOwner;
        Location location;
        Location location2;

        location = Location.builder()
                .state("CA")
                .street("1111 Street A")
                .city("Los Angeles")
                .zipCode("12345")
                .build();

        location = locationRepository.save(location);

        location2 = Location.builder()
                .state("CA")
                .street("2222 Street B")
                .city("Los Angeles")
                .zipCode("12345")
                .build();

        location2 = locationRepository.save(location2);

        restaurant = Restaurant.builder()
                .restaurantOwner(owner)
                .location(location)
                .priceRating(1)
                .rating(5)
                .name("KFC")
                .restaurantStatus(resStatus)
                .build();

        restaurant2 = Restaurant.builder()
                .restaurantOwner(owner)
                .location(location2)
                .priceRating(2)
                .rating(3)
                .name("MCDonald's")
                .restaurantStatus(resStatus)
                .build();

        restaurant = restaurantRepository.save(restaurant);
        restaurant2 = restaurantRepository.save(restaurant2);

//        RestaurantCategoryID resCatID = RestaurantCategoryID.builder().categoryId("American").restaurantId(restaurant.getId()).build();
//        RestaurantCategory resCat = RestaurantCategory.builder().id(resCatID).build();
//        restaurantCategoryRepository.save(resCat);

        for (int i = 0; i < 5; i++){
            BigDecimal bd = BigDecimal.valueOf((i + 1F) * (float) Math.random() + 3)
                    .setScale(2, RoundingMode.HALF_UP);
            Float price = bd.floatValue();

            MenuItem menuItem = MenuItem.builder()
                    .name("MenuItem-"+i)
                    .price(price)
                    .description("Menu Item for a restaurant")
                    .build();

            menuItem.setRestaurant(restaurant);
            menuItemRepository.save(menuItem);
        }
        for (int i = 0; i < 5; i++){
            BigDecimal bd = BigDecimal.valueOf((i + 1F) * (float) Math.random() + 3)
                    .setScale(2, RoundingMode.HALF_UP);
            Float price = bd.floatValue();

            MenuItem menuItem = MenuItem.builder()
                    .name("MenuItem-"+i)
                    .price(price)
                    .description("Menu Item for a restaurant")
                    .build();

            menuItem.setRestaurant(restaurant2);
            menuItemRepository.save(menuItem);
        }

        location = Location.builder()
                .state("CA")
                .street("1111 Customer Location")
                .city("Texas")
                .zipCode("12345")
                .build();

        location = locationRepository.save(location);

        user = UserDetails.builder().firstName("John").lastName("Smith")
                .username("customer").password(passwordEncoder.encode("123456")).email("john@smith.com").phone("1234567890").build();
        customer = Customer.builder().userDetails(user).loyaltyPoints(0).userStatus(status).build();
        customer = customerRepository.save(customer);
        user.setCustomer(customer);
        user = userDetailsRepository.save(user);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<FoodOrder> orders = new ArrayList<>();
        Order order;

        for (int i = 0; i < 20; i++){
            //create customer orders
            order = Order.builder()
                    .orderStatus(orderStatus)
                    .customer(customer)
                    .restaurant(restaurant)
                    .phone("1111111111")
                    .deliveryLocation(location)
                    .deliveryTime(now)
                    .createdAt(now)
                    .preferences("")
                    .foodOrders(orders)
                    .build();

            orderRepository.save(order);
        }

    }
}

