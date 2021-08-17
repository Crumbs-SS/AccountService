package com.crumbs.accountservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.crumbs.accountservice.dto.LoginCredentials;
import com.crumbs.accountservice.exception.LoginException;
import com.crumbs.lib.entity.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String jwtAudience;
    private final String jwtIssuer;
    private final String jwtSecret;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String jwtAudience, String jwtIssuer,
                                   String jwtSecret) {
        this.jwtAudience = jwtAudience;
        this.jwtIssuer = jwtIssuer;
        this.jwtSecret = jwtSecret;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/authenticate");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        if (!req.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + req.getMethod());
        }
        try {
            LoginCredentials creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginCredentials.class);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<LoginCredentials>> errors = validator.validate(creds);
            if (errors.size() > 0) {
                // figure out how to handle exceptions well
                throw new LoginException();
            }

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds, null
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication) {
        UserDetails user = (UserDetails)authentication.getPrincipal();
        String role = user.getPassword();
        String token;
        final long EXPIRATION_TIME = 900_000; // 15 mins
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);
            token = JWT.create()
                    .withAudience(this.jwtAudience)
                    .withIssuer(this.jwtIssuer)
                    .withClaim("role", role)
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            response.addHeader("UserId", user.getId().toString());
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, UserId");
        } catch (JWTCreationException e) {
            e.printStackTrace();
        }
    }
}