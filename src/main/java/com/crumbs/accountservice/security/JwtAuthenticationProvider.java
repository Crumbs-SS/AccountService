package com.crumbs.accountservice.security;

import com.crumbs.accountservice.dto.LoginCredentials;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.UserDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

@Service
@Configurable
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginCredentials creds = (LoginCredentials) authentication.getPrincipal();
        UserDetails user = userDetailsRepository.findByUsername(creds.getUsername()).orElseThrow(NoSuchElementException::new);
        if (!passwordEncoder.matches(creds.getPassword(), user.getPassword())) {
            // TODO: make an invalid password exception
            throw new NoSuchElementException();
        }

        try {
            Field role = UserDetails.class.getDeclaredField(creds.getRole());
            role.setAccessible(true);
            if (null == role.get(user)) {
                // TODO: make an invalid role exception
                throw new NoSuchElementException();
            }
        } catch(Exception e) {
            throw new NoSuchElementException();
        }
        user.setPassword(creds.getRole());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,
                null, authentication.getAuthorities());
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
