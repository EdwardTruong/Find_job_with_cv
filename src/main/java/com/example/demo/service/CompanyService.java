package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CompanyDto;
import com.example.demo.entity.CompanyEntity;

public interface CompanyService {
    List<CompanyEntity> loadAll();
    void save(CompanyEntity newCompany);
    CompanyEntity findCompanyEntityById(Integer Id);
    
    CompanyDto findCompanyDtoByIdToGetDetail(Integer Id);
    void update(CompanyEntity companyEntity);
    boolean setLogo(CompanyEntity companyEntity, MultipartFile multipathFile);
    CompanyDto findCompanyDtoByName(String nameCompany);
    List<Object[]> loadTopCompanyToDto();
    
    List<CompanyDto> loadAllCompanyDto();
    
    
    //Finding function: 
    Page<CompanyDto> findByKeyword(String keyword,int pageNo, int pageSize);
    
    
}
