package com.crumbs.accountservice.utils;

import lombok.Getter;

public class ApiUrl {
    private ApiUrl(){
        throw new IllegalStateException("Utility class");
    }
    @Getter private static final String API_URL = "https://api.crumbs-ss.link";
    @Getter private static final String ORDER_SERVICE_API_URL = API_URL + "/order-service";
    @Getter private static final String EMAIL_SERVICE_API_URL = API_URL + "/email-service";
}
