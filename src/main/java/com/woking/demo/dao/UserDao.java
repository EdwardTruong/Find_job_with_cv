package com.woking.demo.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.woking.demo.entity.UserEntity;

/*
 * 1. The findUserByEmail method has to 2 times.
 * 	 	a.It used to find user in UserDetailServiceImple into database (spring security) .
 * 	 	b.It used to check email before register new a user.
 * 
 * 2. The searchingUser method to find user(s).
 */

@Repository
public interface UserDao extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserByEmail(String email);

    @Query("SELECT u FROM UserEntity u "
    		+ "WHERE (CONCAT(u.fullName,' ',u.email,' ',u.phoneNumber,' ',u.address) LIKE %?1%)"
    		+ "OR u.fullName LIKE %?1% OR u.email LIKE %?1% OR u.phoneNumber LIKE %?1% OR u.address LIKE %?1% ")
    Page<UserEntity> searchingUser(String input, Pageable pageable);
}
