package com.restcourse.service.impl;

import com.restcourse.entity.AddressEntity;
import com.restcourse.entity.UserEntity;
import com.restcourse.repository.AddressRepository;
import com.restcourse.repository.UserRepository;
import com.restcourse.service.AddressService;
import com.restcourse.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = userRepo.findByUserId(userId);
        if(userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepo.findAll();
        for(AddressEntity addressEntity : addresses){
            returnValue.add(modelMapper.map(addressEntity,AddressDto.class));
        }
        return returnValue;
    }
}
