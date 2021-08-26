package com.crumbs.accountservice.security;

import com.crumbs.AuthLib.security.JwtAuthorizationFilter;
import com.crumbs.accountservice.exception.ExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.audience}")
    private String jwtAudience;

    @Autowired
    ExceptionHelper helper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ExceptionHandlerFilter(helper), JwtAuthenticationFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtAudience, jwtIssuer, jwtSecret))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtSecret))
                .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/customers/*").permitAll()
                .antMatchers("/drivers/**").permitAll()
                .antMatchers("/drivers*").permitAll()
                .antMatchers("/users*").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/owners/*").permitAll()
                .antMatchers("/customers*").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().and().csrf().disable(); // Acceptable because we are using JWT in bearer token
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
