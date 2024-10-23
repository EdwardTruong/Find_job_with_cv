package com.woking.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.woking.demo.dao.CvDao;
import com.woking.demo.dto.CvDto;
import com.woking.demo.entity.CvEntity;
import com.woking.demo.exception.StorageException;
import com.woking.demo.mapper.CvMapper;
import com.woking.demo.utils.ApplicationUtils;

import jakarta.transaction.Transactional;

@Service
public class CvServiceImpl implements CvService {

	@Autowired
	CvDao cvDao;

	@Autowired
	CvMapper cvMapper;

	@Autowired
	ApplicationUtils utils;

	@Autowired
	FileService fileService;

	@Override
	@Transactional
	public void upLoadteCv(CvEntity cvEntity) {
		cvDao.saveAndFlush(cvEntity);
	}

	@Override
	public CvEntity findById(Integer id) {
		Optional<CvEntity> result = cvDao.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}
	


	@Override
	@Transactional
	public void deleteCvEntity(CvEntity cvEntity) {
		cvDao.deleteById(cvEntity.getId());
	}

	@Override
	@Transactional
	public CvEntity storedCvToLocalAndDB(MultipartFile file, Integer userId) {

		try {
			String fileName = fileService.storedFileToLocal(file, userId);
			CvEntity newCv = new CvEntity();
			newCv.setContent(file.getBytes());
			newCv.setName(fileName);

			cvDao.save(newCv);

			return newCv;

		} catch (IOException e) {
			throw new StorageException("Failed to store file into database.", e);// Controller filter have some messeger
																					// to hander this throw
		}
	}

	@Override
	public List<CvDto> findCvByUserId(Integer userId) {
		List<CvEntity> listCvEntity = cvDao.findByUserId(userId);
		return (List<CvDto>) cvMapper.toListDto(listCvEntity);
	}

	@Override
	public CvEntity findByName(String name) {
		Optional<CvEntity> result = cvDao.findByName(name);
		return result.orElse(null);
	}



}
