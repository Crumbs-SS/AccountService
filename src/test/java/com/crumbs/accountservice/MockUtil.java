package com.crumbs.accountservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.crumbs.accountservice.dto.*;
import com.crumbs.lib.entity.Driver;
import com.crumbs.lib.entity.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class MockUtil {

    public static UserDetails getUser(){
        return UserDetails.builder()
                .username("testguy")
                .id(-1L)
                .build();
    }
    public static String getUsername(){
        return "correctUsername";
    }
    public static CustomerRegistration getCustomerRegistration(){
        return CustomerRegistration.builder()
                .email("mock@123.com")
                .firstName("mock")
                .lastName("mock")
                .username("correctUsername")
                .password("123456")
                .phone("1231231234")
                .build();
    }
    public static OwnerRegistration getOwnerRegistration(){
        return OwnerRegistration.builder()
                .email("mock@123.com")
                .firstName("mock")
                .lastName("mock")
                .username("correctUsername")
                .password("123456")
                .phone("1231231234")
                .build();

    }
    public static DriverRegistration getDriverRegistration(){
        return DriverRegistration.builder()
                .email("mock@123.com")
                .firstName("mock")
                .lastName("mock")
                .username("correctUsername")
                .password("123456")
                .phone("1231231234")
                .licenseId("1234567")
                .build();

    }
    public static ChangePasswordDTO getChangePasswordDTO(){
        return ChangePasswordDTO.builder().password("password").confirmationToken("token").build();
    }

    public static CustomerDeleteCredentials getCred(){
        return CustomerDeleteCredentials.builder()
                .username("correctUsername")
                .password("testguy12")
                .build();
    }

    public static UserDetailsUpdate getUserDetailsUpdate(){
        return UserDetailsUpdate.builder()
                .username("correctUsername")
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
    public  static String createMockJWT(String role){
        final long EXPIRATION_TIME = 900_000;
        String token;
        Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
        token = JWT.create()
                .withAudience("crumbs")
                .withIssuer("Crumbs")
                .withClaim("role", role)
                .withSubject("correctUsername")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);

        return token;
    }
}
