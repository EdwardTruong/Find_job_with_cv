package com.woking.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.woking.demo.dao.ApplyjobDao;
import com.woking.demo.dto.ApplyjobDto;
import com.woking.demo.entity.Applyjob;
import com.woking.demo.entity.RecruitmentEntity;
import com.woking.demo.entity.UserEntity;
import com.woking.demo.mapper.ApplyjobMapper;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.transaction.Transactional;

@Service
public class ApplyjobServiceImple implements ApplyjobService {

	@Autowired
	ApplyjobDao applyjobDao;

	@Autowired
	ApplicationUtils util;

	@Autowired
	ApplyjobMapper applyjobMapper;

	@Override
	public List<Applyjob> loadAll() {
		return (List<Applyjob>) applyjobDao.findAll();
	}

	@Override
	public List<ApplyjobDto> loadAllToDto() {
		List<Applyjob> listEntity = loadAll();
		return applyjobMapper.getListApplyjobDto(listEntity);
	}
	

	@Override
	@Transactional
	public void save(Applyjob aj) {
		applyjobDao.save(aj);
	}
	
	@Override
	public void saveAll(List<Applyjob> listSaveJob) {
		applyjobDao.saveAll(listSaveJob);
	}

	@Override
	public Page<Applyjob> listApplyJobByIdRe(Integer idRe, int pageNo,int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return applyjobDao.findAllApplyJobByRecruitmentId(idRe, pageable);
	}

	@Override
	public void delete(Applyjob Applyjob) {
		applyjobDao.delete(Applyjob);
	}

	@Override
	public void save(UserEntity user, String cvName, RecruitmentEntity recruitment, String text) {
		Applyjob Applyjob = new Applyjob();
		Applyjob.setNameCv(cvName);
		Applyjob.setRecruitment(recruitment);
		Applyjob.setUser(user);
		Applyjob.setCreateAt(util.getCurrentDateSQL());
		Applyjob.setStatus(false);
		Applyjob.setText(text);
		applyjobDao.save(Applyjob);
	}

	@Override
	public Page<Applyjob> listUserApplied(Integer userId, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").descending());
		return applyjobDao.findAllByUserId(userId, pageable);
	}

	@Override
	public Page<Applyjob> appliesOfUsersByCompanyId(Integer idCompany, int pageNo, int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").ascending());
		return applyjobDao.findAppliedUsersByCompanyId(idCompany, pageable);
	}

	@Override
	public Applyjob findById(Integer idApply) {
		Optional<Applyjob> result = applyjobDao.findById(idApply);
		return result.orElse(null);
	}

	/*
	 * Using update project later not on this cource
	 */

	@Override
	public Applyjob findApplyJobbyidUserdAndidRe(Integer userId, Integer idRe) {
		return applyjobDao.findByUserIdAndRecruitmentId(userId, idRe).orElse(null);

	}





}
