package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.FollowCompany;
import com.example.demo.entity.UserEntity;

public interface FollowCompanyService {
     void save(UserEntity user, CompanyEntity company);
     FollowCompany findFollowByUserIdAndCompanyId(Integer userId, Integer companyId);
     FollowCompany findById(Integer id);
     void delete(FollowCompany fl);
     Page<FollowCompany> findAllFollowOfUser(Integer userId, int pageNo,int pageSize);
}
