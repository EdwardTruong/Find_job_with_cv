package com.woking.demo.service;

import org.springframework.data.domain.Page;

import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.FollowCompany;
import com.woking.demo.entity.UserEntity;

public interface FollowCompanyService {
     void save(UserEntity user, CompanyEntity company);
     FollowCompany findFollowByUserIdAndCompanyId(Integer userId, Integer companyId);
     FollowCompany findById(Integer id);
     void delete(FollowCompany fl);
     Page<FollowCompany> findAllFollowOfUser(Integer userId, int pageNo,int pageSize);
}
