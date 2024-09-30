package com.example.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RecruitmentEntity;

@Repository
public interface RecruitmentDao extends JpaRepository<RecruitmentEntity, Integer> {
    
    @Query("SELECT r FROM RecruitmentEntity r WHERE r.companyEntity.id =:input")
    Page<RecruitmentEntity> findAllEntityById(@Param("input")Integer input, Pageable pageable);

    @Query("SELECT r FROM RecruitmentEntity r WHERE r.companyEntity.id =:input")
    List<RecruitmentEntity> findAllEntityByCompnyId(@Param("input")Integer input);


    @Query("SELECT r FROM RecruitmentEntity r WHERE r.category.id =:input")
    Page<RecruitmentEntity> findRecruitmentsSameCategoryId(@Param("input") Integer categoryId, Pageable pageable);

    // I will make more with field experience later.
    @Query("SELECT r FROM RecruitmentEntity r WHERE r.status = TRUE ORDER BY r.createdAt DESC, r.salary DESC, r.quantity DESC , r.view DESC LIMIT 5")
    List<RecruitmentEntity> findTopRecruitment();

    @Query("SELECT r FROM RecruitmentEntity r "
    		+ "WHERE r.title LIKE %?1% OR r.description LIKE %?1% "
    		+"OR r.address LIKE %?1%"
    		+ "OR (CONCAT(r.title,' ',r.description) LIKE %?1% )")
    List<RecruitmentEntity> searchingTitle(String input);

    @Query("SELECT r FROM RecruitmentEntity r WHERE r.address LIKE %?1%")
    Page<RecruitmentEntity> searchingAddress(String input, Pageable pageable);
}
