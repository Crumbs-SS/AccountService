package com.crumbs.accountservice.utils;

public class ApiUrl {
    public static String getApiUrl(){
        return "https://api.crumbs-ss.link";
    }
    public static String getOrderServiceUrl(){
        return getApiUrl() + "/order-service";
    }
}
