package com.restcourse.repository;

import com.restcourse.entity.AddressEntity;
import com.restcourse.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AddressRepository extends PagingAndSortingRepository<AddressEntity,Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
}
