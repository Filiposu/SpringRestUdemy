package com.restcourse.service.impl;

import com.restcourse.entity.UserEntity;
import com.restcourse.excepitions.UserServiceException;
import com.restcourse.repository.UserRepository;
import com.restcourse.service.UserService;
import com.restcourse.shared.Utils;
import com.restcourse.shared.dto.AddressDto;
import com.restcourse.shared.dto.UserDto;
import com.restcourse.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepo.findByEmail(user.getEmail()) !=null) throw new RuntimeException("Record already exists");

        for (int i = 0; i < user.getAddresses().size(); i++) {
            AddressDto address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(30));
            user.getAddresses().set(i,address);
        }
        
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user,UserEntity.class);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity storedUser =  userRepo.save(userEntity);

        UserDto returnValue = modelMapper.map(storedUser,UserDto.class);
        
        return  returnValue;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity = userRepo.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnvalue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnvalue);
        return returnvalue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepo.findByUserId(userId);

        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId,UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepo.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        UserEntity updatedUser = userRepo.save(userEntity);
        BeanUtils.copyProperties(updatedUser,returnValue);
        return returnValue;

    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepo.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepo.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page,int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        Page<UserEntity> usersPage =  userRepo.findAll(PageRequest.of(page,limit));
        List<UserEntity> users = usersPage.getContent();
        for(UserEntity user : users){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user,userDto);
            returnValue.add(userDto);
        }
        return  returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       UserEntity userEntity =  userRepo.findByEmail(email);
       if(userEntity == null) throw new UsernameNotFoundException(email);
       return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
