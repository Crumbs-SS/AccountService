package com.crumbs.accountservice.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public class RestTemplateUtils {
    private RestTemplateUtils(){
        throw new IllegalStateException("Utility class");
    }
    public static HttpEntity<String> getHeaders(Map<String, String> headerMaps){
        HttpHeaders headers = new HttpHeaders();
        headerMaps.forEach(headers::add);

        return new HttpEntity<>(headers);
    }
}
