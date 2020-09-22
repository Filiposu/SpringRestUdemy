package com.restcourse.repository;

import com.restcourse.entity.UserEntity;
import com.restcourse.shared.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity,Long> {

    //UserEntity findUserByEmail(String email);
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
