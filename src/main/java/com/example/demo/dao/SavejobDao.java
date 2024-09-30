package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.SaveJob;

@Repository
public interface SavejobDao extends JpaRepository<SaveJob, Integer>{
    Optional<SaveJob> findByUserIdAndRecruitmentId(int userId, int recruitId );

    List<SaveJob> findAllByUserId(int userId);
    Page<SaveJob> findAllByUserId(int userId, Pageable pageable);

    
    /* 
    * SELECT * FROM save_job s
    * JOIN `recruitment` r ON r.id = s.`recruitment_id`
    * JOIN `company` c ON c.id = r.`company_id`
    * WHERE c.id = 1 ; 
    */
    @Query("SELECT s FROM SaveJob s " +
     "JOIN RecruitmentEntity r ON r.id = s.recruitment.id "+ 
     "JOIN CompanyEntity c ON c.id = r.companyEntity.id "+
     "WHERE c.id = :idCompany")
    Page<SaveJob> findSaveJobByCompanyId(@Param("idCompany")int idCompany, Pageable pageable );


}
