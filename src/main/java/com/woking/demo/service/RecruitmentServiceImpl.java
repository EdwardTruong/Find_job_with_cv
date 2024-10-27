package com.woking.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.woking.demo.dao.RecruitmentDao;
import com.woking.demo.dto.RecruitmentDto;
import com.woking.demo.dto.RecruitmentResponseFindDto;
import com.woking.demo.entity.RecruitmentEntity;
import com.woking.demo.mapper.RecruitmentMapper;

import jakarta.transaction.Transactional;

/**
 * RecruitmentServiceImpl
 */
@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    @Autowired
    RecruitmentDao rDao;

    @Autowired
    CompanyService companyService;

    @Autowired
    RecruitmentMapper recruitmentMapper;

    @Override
    public List<RecruitmentEntity> loadAll() {
        return (List<RecruitmentEntity>) rDao.findAll();
    }

    @Override
    @Transactional
    public void save(RecruitmentEntity entity) {
        rDao.save(entity);
    }

    @Override
    @Transactional
    public void update(RecruitmentEntity entity) {
        rDao.saveAndFlush(entity);
    }

    @Override
    public RecruitmentEntity findRecruitmenById(Integer id) {
        Optional<RecruitmentEntity> result = rDao.findById(id);
        return result.orElse(null);
    }

    @Override
    @Transactional
    public void delete(RecruitmentEntity recruitmentEntity) {
        rDao.delete(recruitmentEntity);
    }

    @Override
    public Page<RecruitmentEntity> listAllRecruitmentPageable(Integer id, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").ascending());
        Page<RecruitmentEntity> listEntity = rDao.findAllEntityById(id, pageable);
        return listEntity;
    }

    @Override
    public List<RecruitmentDto> loadAllToDto() {
        List<RecruitmentEntity> listEntity = loadAll();
        return recruitmentMapper.getListRecruitmentDto(listEntity);
    }

    @Override
    public List<RecruitmentDto> findTopRecruitment() {
        List<RecruitmentEntity> listEntity = rDao.findTopRecruitment();
        return recruitmentMapper.getListRecruitmentDto(listEntity);
    }

    @Override
    public Page<RecruitmentEntity> findRecruitmentDtoSameCategoryId(Integer categoryId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return rDao.findRecruitmentsSameCategoryId(categoryId, pageable);
    }

    @Override
    public List<RecruitmentDto> listRecruitmentDtoByCompanyId(Integer id) {
        List<RecruitmentEntity> listEntity = rDao.findAllEntityByCompnyId(id);
        return recruitmentMapper.getListRecruitmentDto(listEntity);
    }

    @Override
    public Page<RecruitmentResponseFindDto> findReBySearchingTittle(String keySearch, int pageNo, int pageSize) {

        List<RecruitmentResponseFindDto> listRe = rDao.searchingTitle(keySearch);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());

        int start = (int) pageable.getOffset();

        if (pageNo > 1) {
            start = 0;
        }

        int end = (int) (pageable.getOffset() + pageable.getPageSize() > listRe.size() ? listRe.size()
                : (pageable.getOffset() + pageable.getPageSize()));

        List<RecruitmentResponseFindDto> newLisst = listRe.subList(start, end);

        return new PageImpl<>(newLisst, pageable, listRe.size());

    }

    @Override
    public List<RecruitmentResponseFindDto> findReBySearchingBar(String keySearch) {
        return rDao.searchingTitle(keySearch);
    }

    @Override
    public Page<RecruitmentResponseFindDto> findReBySearchingAddress(String keySearch, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        return rDao.searchingAddress(keySearch, pageable);
    }

}