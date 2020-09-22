package com.restcourse.ui.controller;

import com.restcourse.excepitions.UserServiceException;
import com.restcourse.service.UserService;
import com.restcourse.shared.dto.AddressDto;
import com.restcourse.shared.dto.UserDto;
import com.restcourse.ui.model.request.UserDetailsRequestModel;
import com.restcourse.ui.model.response.AddressesRest;
import com.restcourse.ui.model.response.ErrorMessages;
import com.restcourse.ui.model.response.OperationStatusModel;
import com.restcourse.ui.model.response.UserRest;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}",produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE} )
    public UserRest getUser(@PathVariable String id){
        UserRest returnValue = new UserRest();
        System.out.println("inside getUSer");
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto,returnValue);
        return returnValue;
    }



    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();
        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(String.valueOf(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage()));

//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails,userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails,UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser,UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{userId}")
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails,@PathVariable String userId){
        UserRest returnValue = new UserRest();
        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(String.valueOf(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage()));

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);

        UserDto updateUser = userService.updateUser(userId,userDto);
        BeanUtils.copyProperties(updateUser,returnValue);
        return returnValue;
    }

    @DeleteMapping(path = "{id}")
    public OperationStatusModel deleteUser(@PathVariable String id){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "limit",defaultValue = "25") int limit){
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page,limit);
        for(UserDto user : users)
        {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(user,userModel);
            returnValue.add(userModel);
        }
        return returnValue;

    }

    @GetMapping(path = "/{id}/addresses")
    public List<AddressesRest> getUserAddresses(@PathVariable String id){
        List<AddressesRest> returnValue = new ArrayList<>();
        List<AddressDto> addressDto = null;

        return returnValue;
    }

}
