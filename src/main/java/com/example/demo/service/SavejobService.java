package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.entity.RecruitmentEntity;
import com.example.demo.entity.SaveJob;
import com.example.demo.entity.UserEntity;

/*
 *   1.The method CRUD : findById,save,delete.
 *   2.The findJobSavedByUserIdAndRecruitmentId method using check post was saved by user yet. 
  *  3.The findAllJobSavedByUserId method use to manage post saved and pagination.
 *   4.The findAllJobSavedByCompanyId method using update project later not on this cource
 *   
 */


public interface SavejobService {
    SaveJob findById(Integer id);
    void save(UserEntity user, RecruitmentEntity recruitmentEntity);
    void delete(SaveJob saveJob);
    void saveAll(List<SaveJob> listSaveJobs);
    SaveJob findJobSavedByUserIdAndRecruitmentId(Integer userId, Integer recuitmentId);
    
    Page<SaveJob> findAllJobSavedByUserId(Integer userId ,int pageNo, int PageSize);
   
    Page<SaveJob> findAllJobSavedByCompanyId(Integer idCompany, int pageNo ,int pageSize);
    
}
