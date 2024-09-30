package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.RoleEntity;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    
}
