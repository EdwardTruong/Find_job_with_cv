package com.woking.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.woking.demo.dao.FollowCompanyDao;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.entity.FollowCompany;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.service.FollowCompanyService;

@Service
public class FollowcompanyServiceImpl implements FollowCompanyService {

    @Autowired
    FollowCompanyDao flDao;

    @Override
    public FollowCompany findFollowByUserIdAndCompanyId(Integer userId, Integer companyId) {
       return flDao.findByUserIdAndCompanyId(userId, companyId).orElse(null);
    }

    @Override
    public void save(UserEntity user, CompanyEntity company) {
        FollowCompany followoCompany = new FollowCompany();
        followoCompany.setUser(user);
        followoCompany.setCompany(company);
        flDao.save(followoCompany);
    }

    @Override
    public void delete(FollowCompany fl) {
        flDao.delete(fl);
    }

    @Override
    public FollowCompany findById(Integer id) {
       Optional<FollowCompany> result = flDao.findById(id);
       if(result.isPresent()){
        return result.get(); 
       }
       return null;
    }

    @Override
    public Page<FollowCompany> findAllFollowOfUser(Integer userId, int pageNo,int pageSize)  {
    	
    	
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("id").descending());
        return flDao.findByUserId(userId,pageable);
    }
    
}
