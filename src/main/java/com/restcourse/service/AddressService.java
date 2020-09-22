package com.restcourse.service;

import com.restcourse.shared.dto.AddressDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AddressService {
    List<AddressDto> getAddresses(String userId);
}
