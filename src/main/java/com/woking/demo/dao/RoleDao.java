package com.woking.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woking.demo.entity.RoleEntity;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    
}
