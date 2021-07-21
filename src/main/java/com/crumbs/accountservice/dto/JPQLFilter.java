package com.crumbs.accountservice.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
@Builder
public class JPQLFilter {
    private PageRequest pageRequest;
    private String query;
}
