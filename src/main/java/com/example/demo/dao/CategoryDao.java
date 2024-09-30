package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CategoryEntity;



@Repository
public interface CategoryDao extends JpaRepository<CategoryEntity,Integer>{
    
     Optional<CategoryEntity> findByName(String name); // Using update project later not on this cource

    @Query("SELECT c FROM CategoryEntity c ORDER BY c.numberChoose DESC, c.name ASC LIMIT 4")
    List<CategoryEntity> topGateroriesByNumberChoose();
}
