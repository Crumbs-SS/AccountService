package com.crumbs.accountservice.security;

import com.crumbs.AuthLib.security.JwtAuthorizationFilter;
import com.crumbs.accountservice.exception.ExceptionHelper;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    ExceptionHelper helper;

    String JWT_SECRET = System.getenv("JWT_SECRET");
    String JWT_ISSUER = System.getenv("JWT_ISSUER");
    String JWT_AUDIENCE = System.getenv("JWT_AUDIENCE");

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ExceptionHandlerFilter(helper), JwtAuthenticationFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), JWT_AUDIENCE, JWT_ISSUER, JWT_SECRET))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), JWT_SECRET))
                .authorizeRequests()
                .antMatchers("/account-service/authenticate","/account-service/register/**", "/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().and().csrf().disable(); // Acceptable because we are using JWT in bearer token
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedOrigins(List.of(
                "http://crumbs-client.s3-website-us-east-1.amazonaws.com",
                "http://crumbs-admin-bucket-01.s3-website-us-east-1.amazonaws.com",
                "http://localhost:3000"
        ));

        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
