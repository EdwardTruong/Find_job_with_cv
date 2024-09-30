package com.example.demo.seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.CategoryEntity;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CompanyService;
import com.example.demo.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class DataSeeder {
    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @PostConstruct
    public void createdCategoryEntity(){
        CategoryEntity cPluts = new CategoryEntity("C++");
        CategoryEntity c = new CategoryEntity("C");
        CategoryEntity cSharp = new CategoryEntity("C#");
        CategoryEntity java = new CategoryEntity("Java");
        CategoryEntity javaScrip = new CategoryEntity("Javascript");
        CategoryEntity PHP = new CategoryEntity("PHP");
        CategoryEntity golang = new CategoryEntity("Golang");
        CategoryEntity swift = new CategoryEntity("Swift");
        CategoryEntity ruby = new CategoryEntity("Ruby");
        CategoryEntity python = new CategoryEntity("Python");


        List<CategoryEntity> listCategoryEntity = new ArrayList<>(Arrays.asList(cPluts,c,cSharp,java,javaScrip,PHP,golang,swift,ruby,python));
        List<CategoryEntity> newList = categoryService.checkList(listCategoryEntity, python);
        categoryService.saveAll(newList);
    }

}
