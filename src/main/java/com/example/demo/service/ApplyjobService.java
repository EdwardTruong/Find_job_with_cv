package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dto.ApplyjobDto;
import com.example.demo.entity.Applyjob;
import com.example.demo.entity.RecruitmentEntity;
import com.example.demo.entity.UserEntity;

/*
 * 
 */

public interface ApplyjobService {
	
	
    List<Applyjob> loadAll();

    List<ApplyjobDto> loadAllToDto();
    

    void save(Applyjob aj);
    
    void saveAll(List<Applyjob> listSaveJob);

    Applyjob findById(Integer idApply);

    void save(UserEntity user, String cvName, RecruitmentEntity recruitmentEntity, String text);
    
    void delete (Applyjob Applyjob);

    Page<Applyjob> listApplyJobByIdRe(Integer idRe, int pageNo, int pageSize);
    
    Page<Applyjob> listUserApplied(Integer userId, int pageNo,int pageSize);
    
    Page<Applyjob> appliesOfUsersByCompanyId(Integer idCompany, int pageNo,int pageSize);

    /*
    * Using update project later not on this cource
    */

    Applyjob findApplyJobbyidUserdAndidRe(Integer userId, Integer idRe);
}
