package com.crumbs.accountservice.security;

import com.crumbs.accountservice.dto.LoginCredentials;
import com.crumbs.accountservice.exception.EmailNotConfirmedException;
import com.crumbs.accountservice.exception.LoginException;
import com.crumbs.lib.entity.ConfirmationToken;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.ConfirmationTokenRepository;
import com.crumbs.lib.repository.UserDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Configurable
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginCredentials creds = (LoginCredentials) authentication.getPrincipal();
        UserDetails user = userDetailsRepository.findByUsername(creds.getUsername()).orElseThrow(LoginException::new);
        if (!passwordEncoder.matches(creds.getPassword(), user.getPassword())) {
            throw new LoginException();
        }

        if (creds.getRole().equals("driver")) {
            if (null == user.getDriver()) {
                throw new LoginException();
            }
            else if (user.getDriver().getUserStatus().getStatus().equals("PENDING_REGISTRATION")) {
                // email logic TODO
                throw new EmailNotConfirmedException();
            }
            else if (!user.getDriver().getUserStatus().getStatus().equals("REGISTERED")) {
                throw new LoginException();
            }
        }

        else if (creds.getRole().equals("owner")) {
            if (null == user.getOwner()) {
                throw new LoginException();
            }
            else if (user.getOwner().getUserStatus().getStatus().equals("PENDING_REGISTRATION")) {
                // email logic TODO
                throw new EmailNotConfirmedException();
            }
            else if (!user.getOwner().getUserStatus().getStatus().equals("REGISTERED")) {
                throw new LoginException();
            }
        }

        else if (creds.getRole().equals("admin")) {
            if (null == user.getAdmin()) {
                throw new LoginException();
            }
            else if (user.getAdmin().getUserStatus().getStatus().equals("PENDING_REGISTRATION")) {
                // email logic TODO
                throw new EmailNotConfirmedException();
            }
            else if (!user.getAdmin().getUserStatus().getStatus().equals("REGISTERED")) {
                throw new LoginException();
            }
        }

        else if(creds.getRole().equals("customer")) {
            if (null == user.getCustomer()) {
                throw new LoginException();
            }
            else if(user.getCustomer().getUserStatus().getStatus().equals("PENDING_REGISTRATION")){
                String token = UUID.randomUUID().toString();

                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                );

                confirmationTokenRepository.save(confirmationToken);
                String url = "http://localhost:8100/email/" + user.getEmail() + "/name/" + user.getFirstName() + "/token/" + token;
                String result = restTemplate.getForObject(url,String.class);
                throw new EmailNotConfirmedException();
            }
            else if (!user.getCustomer().getUserStatus().getStatus().equals("REGISTERED")) {
                throw new LoginException();
            }
        }

        try {
            Field role = UserDetails.class.getDeclaredField(creds.getRole());
            role.setAccessible(true);
            if (null == role.get(user)) {
                // TODO: make an invalid role exception
                throw new LoginException();
            }
        } catch(Exception e) {
            throw new LoginException();
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
