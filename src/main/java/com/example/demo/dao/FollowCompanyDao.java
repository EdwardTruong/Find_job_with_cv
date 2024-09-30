package com.example.demo.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FollowCompany;

@Repository
public interface FollowCompanyDao extends JpaRepository<FollowCompany, Integer> {
    
    Optional<FollowCompany> findByUserIdAndCompanyId(int userId, int companyId);
    Page<FollowCompany> findByUserId(Integer id, Pageable pageable);
}
