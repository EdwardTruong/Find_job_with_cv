package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="category")
@Getter
@Setter
@NoArgsConstructor
public class CategoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    

    @Column(name="category_name")
    private String name;

    @Column(name="number_choose")
    private int numberChoose;

    @OneToMany(cascade = CascadeType.ALL,mappedBy="category")
    private List<RecruitmentEntity> listRecruitmentEntity;

    public CategoryEntity(String name) {
        this.name = name;
    } 
    
    public void addRecruitment(RecruitmentEntity recruitmentEntity){
        if(listRecruitmentEntity == null){
            listRecruitmentEntity = new ArrayList<>();
        }
        listRecruitmentEntity.add(recruitmentEntity);
        recruitmentEntity.setCategory(this);
    }

}