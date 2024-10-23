package com.woking.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;


import com.woking.demo.dto.RecruitmentDto;
import com.woking.demo.entity.RecruitmentEntity;

/*
 * 
 */


public interface RecruitmentService {
    List<RecruitmentEntity> loadAll();
    List<RecruitmentDto> loadAllToDto();
    public void save(RecruitmentEntity entity);
    public void update(RecruitmentEntity entity);
    public RecruitmentEntity findRecruitmenById(Integer id);
   
    public void delete (RecruitmentEntity recruitmentEntity);

    List<RecruitmentDto> listRecruitmentDtoByCompanyId (Integer id);
    Page<RecruitmentEntity> listAllRecruitmentPageable( Integer id,int pageNo, int pageSize);
    List<RecruitmentDto> findTopRecruitment();
    Page<RecruitmentEntity> findRecruitmentDtoSameCategoryId(Integer categoryId, int pageNo,int pageSize);

    
    Page<RecruitmentEntity> findReBySearchingTittle(String keySearch, int pageNo,int pageSize);
    List<RecruitmentEntity> findReBySearchingBar(String keySearch);
    Page<RecruitmentEntity> findReBySearchingAddress(String keySearch, int pageNo, int pageSize);
}
