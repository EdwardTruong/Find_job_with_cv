package com.woking.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woking.demo.dao.CategoryDao;
import com.woking.demo.entity.CategoryEntity;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    @Transactional
    public List<CategoryEntity> loadAll() {
        return (List<CategoryEntity>)categoryDao.findAll();
    }

    @Override
    @Transactional
    public void saveAll(List<CategoryEntity> listCategory) {
        System.out.println("Check in from  CategoryServiceImple  : saveAll :" + listCategory.size());
        categoryDao.saveAll(listCategory);
    }


    @Override
    public List<CategoryEntity> checkList(List<CategoryEntity> exitsEntity, CategoryEntity newCategory) {
        List<CategoryEntity> newList = new ArrayList<>();
        List<CategoryEntity> allCategory = loadAll();

        String categoryNameLowerCase = newCategory.getName();

        for(CategoryEntity  c : allCategory){
            if(!c.getName().toLowerCase().equals(categoryNameLowerCase)){
                newList.add(c);
           }
        }
        
        if(allCategory.size() <= 2){
           for(CategoryEntity  c : exitsEntity){
            if(!c.getName().toLowerCase().equals(categoryNameLowerCase)){
                newList.add(c);
            }
           }
            return newList;
        }
        return newList;

    }

    @Override
    @Transactional
    public void update(CategoryEntity categoryEntity) {
      categoryDao.saveAndFlush(categoryEntity);
    }

    @Override
    public void save(CategoryEntity categoryEntity) {
      categoryDao.save(categoryEntity);
    }

    @Override
    public List<CategoryEntity> topCategoryEntitiesByNumberChoose() {
        return (List<CategoryEntity>) categoryDao.topGateroriesByNumberChoose();
    }

    /*
     *  Using update project later not on this cource
     */
    @Override
    public CategoryEntity findByName(String categoryName) {
        Optional<CategoryEntity> result = categoryDao.findByName(categoryName);
        return result.orElse(null);
    }

    @Override 
    public CategoryEntity findById(Integer id) {
        Optional<CategoryEntity> result = categoryDao.findById(id);
        return result.orElse(null);
    }

    
}
