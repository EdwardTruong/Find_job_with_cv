package com.woking.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.woking.demo.dao.CompanyDao;
import com.woking.demo.dto.CompanyDto;
import com.woking.demo.dto.CompanyInfoDto;
import com.woking.demo.dto.CompanyResponseFindDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.CompanyEntity;
import com.woking.demo.mapper.CompanyMapper;
import com.woking.demo.service.CompanyService;
import com.woking.demo.service.FileService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CompanyServiceImpl implements CompanyService {

  @Autowired
  CompanyDao companyDao;

  @Autowired
  CompanyMapper companyMapper;

  @Autowired
  FileService fileService;

  @Override
  @Transactional
  public void save(CompanyEntity newCompany) {
    companyDao.save(newCompany);
  }

  @Override
  @Transactional
  public CompanyEntity findCompanyEntityById(Integer id) {
    Optional<CompanyEntity> result = companyDao.findById(id);
    if (result.isPresent()) {
      return result.get();
    }
    return null;
  }

  @Override
  public CompanyDto findCompanyDtoByIdToGetDetail(Integer id) {
    CompanyEntity entity = findCompanyEntityById(id);
    CompanyDto dto = companyMapper.toDtoGetDetails(entity);
    return dto;
  }

  @Override
  public void update(CompanyEntity companyEntity) {
    companyDao.saveAndFlush(companyEntity);
  }

  @Override
  public boolean setLogo(CompanyEntity companyEntity, MultipartFile multipathFile) {

    String logo = fileService.storedFileToLocal(multipathFile, null);

    companyEntity.setLogo(logo);

    update(companyEntity);

    return true;
  }

  @Override
  public List<CompanyEntity> loadAll() {
    return (List<CompanyEntity>) companyDao.findAll();
  }

  @Override
  public List<Object[]> loadTopCompanyToDto() {
    List<Object[]> companies = companyDao.getTopCompanies();
    return companies;
  }

  public List<CompanyDto> loadAllCompanyDto() {
    List<CompanyEntity> listEntity = loadAll();
    return companyMapper.toListCompanyDto(listEntity);
  }

  @Override
  public CompanyDto findCompanyDtoByName(String nameCompany) {
    Optional<CompanyEntity> result = companyDao.findByName(nameCompany);
    if (result.isPresent()) {
      return companyMapper.toDto(result.get());
    }
    return null;
  }

  @Override
  public Page<CompanyDto> findByKeyword(String keyword, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
    Page<CompanyEntity> entities = companyDao.findCompanybyKeyword(keyword, pageable);
    return companyMapper.toListDto(entities);

  }

  @Override
  public Page<CompanyResponseFindDto> findCompanybyKeywordToDTO(String keySearch, Integer pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

    Page<CompanyResponseFindDto> result = companyDao.findCompanybyKeywordToDTO(keySearch, pageable);
    return result;
  }

  @Override
  public CompanyInfoDto findCompanyInfoDtoById(Integer idCompany) {
    CompanyEntity entity = this.findCompanyEntityById(idCompany);
    return companyMapper.toCompanyInfoDto(entity);
  }

  @Override
  public void updateCompanyInfo(CompanyInfoDto dto, UserDto userAuthenticated) {
    CompanyEntity companyEntity = this.findCompanyEntityById(userAuthenticated.getIdCompany());
    companyEntity.setAddress(dto.getAddress());
    companyEntity.setDescription(dto.getDescription());
    companyEntity.setEmail(dto.getEmail());
    companyEntity.setPhoneNumber(dto.getPhoneNumber());
    companyEntity.setLogo(dto.getLogo());
    companyEntity.setName(dto.getName());
    this.update(companyEntity);
  }

}
