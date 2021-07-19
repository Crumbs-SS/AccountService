package com.crumbs.accountservice.service;

import com.crumbs.accountservice.dto.DriverDTO;
import com.crumbs.lib.entity.Driver;
import com.crumbs.lib.entity.UserDetails;
import com.crumbs.lib.repository.DriverRepository;
import com.crumbs.lib.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = { Exception.class })
public class UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public UserService(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserDetailsRepository userDetailsRepository,
                       @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DriverRepository driverRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.driverRepository = driverRepository;
    }

    public UserDetails userById(int userId) {
        return userDetailsRepository.findById((long) userId).orElseThrow();
    }

    public PageRequest getPageRequest(Integer page, Integer pageSize, String sortField, String sortDirection){
        if (sortField.equals("username")) {
            sortField = "userDetails.username";
        }
        Sort by = (sortDirection.equals("asc")) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return sortField!=null ? PageRequest.of(page, pageSize, by) : PageRequest.of(page, pageSize);
    }

    public Page<DriverDTO> getDrivers(PageRequest pageRequest, String searchString, String status){
        try{
            Page<Driver> drivers;
            if (searchString.isEmpty() && status.isEmpty()) {
                // neither string is used
                drivers = driverRepository.findAll(pageRequest);
            }
            else if (!searchString.isEmpty() && !status.isEmpty()) {
                // both strings are used
                drivers = driverRepository.findAllByStatusAndSearchString(status, searchString, pageRequest);
            }
            else if (!searchString.isEmpty()){
                // only searchString is used
                drivers = driverRepository.findAllBySearchString(searchString, pageRequest);
            }
            else {
                // only status is used
                drivers = driverRepository.findAllByStatus(status, pageRequest);
            }
            Page<DriverDTO> d = drivers.map(driver -> driverEntityToDTO(driver));
            return d;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private DriverDTO driverEntityToDTO(Driver d) {
        DriverDTO dto = new DriverDTO();
        dto.setId(d.getId());
        dto.setFirstName(d.getUserDetails().getFirstName());
        dto.setLastName(d.getUserDetails().getLastName());
        dto.setUsername(d.getUserDetails().getUsername());
        dto.setEmail(d.getUserDetails().getEmail());
        dto.setPhone(d.getUserDetails().getPhone());
        dto.setLicenseId(d.getLicenseId());
        dto.setState(d.getState().getState());
        return dto;
    }
}
