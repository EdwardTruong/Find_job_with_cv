package com.woking.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.woking.demo.dto.CompanyDto;
import com.woking.demo.dto.CompanyInfoDto;
import com.woking.demo.dto.CompanyResponseFindDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.CompanyEntity;

import jakarta.validation.Valid;

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
    Page<CompanyResponseFindDto> findCompanybyKeywordToDTO(String keySearch, Integer pageNo, int pageSize);
    CompanyInfoDto findCompanyInfoDtoById(Integer idCompany);
    void updateCompanyInfo(@Valid CompanyInfoDto companyInformation, UserDto userAuthenticated);
    
    
}
