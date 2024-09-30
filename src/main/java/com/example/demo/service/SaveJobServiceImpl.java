package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dao.SavejobDao;
import com.example.demo.entity.RecruitmentEntity;
import com.example.demo.entity.SaveJob;
import com.example.demo.entity.UserEntity;

import jakarta.transaction.Transactional;



@Service
public class SaveJobServiceImpl implements SavejobService {

    @Autowired
    SavejobDao savejobDao;

    @Autowired
    UserService userService;


    @Override
    public void save(UserEntity user, RecruitmentEntity recruitmentEntity) {
        SaveJob newSj = new SaveJob();
        newSj.setId(0);
        newSj.setUser(user);
        newSj.setRecruitment(recruitmentEntity);
        savejobDao.save(newSj);
    }



  @Override
    public SaveJob findJobSavedByUserIdAndRecruitmentId(Integer userId, Integer recuitmentId) {
        Optional<SaveJob> result = savejobDao.findByUserIdAndRecruitmentId(userId, recuitmentId);
        return result.orElse(null);
    }


    @Override
    public void delete(SaveJob sj) {
       savejobDao.delete(sj);
    }

    @Override
    public Page<SaveJob> findAllJobSavedByUserId(Integer userId ,int pageNo, int PageSize ) {
        Pageable pageable = PageRequest.of(pageNo - 1, PageSize, Sort.by("id").descending());
        return savejobDao.findAllByUserId(userId, pageable);
    }

    @Override
    public SaveJob findById(Integer id) {
        Optional<SaveJob> sj = savejobDao.findById(id);
        return sj.orElse(null);
    }

    @Override
    public Page<SaveJob> findAllJobSavedByCompanyId(Integer idCompany, int pageNo ,int pageSize) {
       Pageable pageable = PageRequest.of(pageNo -1 , pageSize);
       return savejobDao.findSaveJobByCompanyId(idCompany, pageable);
    }



	@Override
	@Transactional
	public void saveAll(List<SaveJob> listSaveJobs) {
		savejobDao.saveAll(listSaveJobs);
		
	}





    
    
}
