package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CategoryEntity;


public interface CategoryService {

	public List<CategoryEntity> loadAll();
	
	public void saveAll(List<CategoryEntity> listCategory);

	void save(CategoryEntity categoryEntity);

	void update(CategoryEntity categoryEntity);

	public List<CategoryEntity> checkList(List<CategoryEntity> exitsEntity, CategoryEntity newCategory);

	public List<CategoryEntity> topCategoryEntitiesByNumberChoose();

	/*
	 * Using update project later not on this cource
	 */
	public CategoryEntity findByName(String categoryName);

	public CategoryEntity findById(Integer id);

}
