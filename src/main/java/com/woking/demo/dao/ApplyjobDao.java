package com.woking.demo.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woking.demo.entity.Applyjob;

public interface ApplyjobDao extends JpaRepository<Applyjob, Integer> {
    
    Optional<Applyjob> findByUserIdAndRecruitmentId(int userId, int recruitmentId);// Using update project later not on this cource

    Page<Applyjob> findAllByUserId(Integer userId, Pageable pageable);

    @Query("SELECT a FROM Applyjob a WHERE a.recruitment.id=:idRe")
    Page<Applyjob> findAllApplyJobByRecruitmentId(@Param("idRe") int id, Pageable pageable);
    
    @Query("SELECT a FROM Applyjob a " +
        "JOIN RecruitmentEntity r ON r.id = a.recruitment.id "+
        "JOIN CompanyEntity c ON c.id = r.companyEntity.id "+
        "WHERE c.id = :idCompany " )
    Page<Applyjob> findAppliedUsersByCompanyId(@Param("idCompany") Integer idCompany, Pageable pageable);
}
