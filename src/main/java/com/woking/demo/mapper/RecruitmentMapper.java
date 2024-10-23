package com.woking.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woking.demo.dto.RecruitmentDto;
import com.woking.demo.entity.RecruitmentEntity;
import com.woking.demo.utils.ApplicationUtils;

@Component
public class RecruitmentMapper {
    

    // @Autowired
    // CompanyMapper cMapper;

    @Autowired
    ApplicationUtils util;

    public RecruitmentEntity toEntity(RecruitmentDto dto){
        return RecruitmentEntity.builder()
            .id(dto.getId())
            .address(dto.getAddress())
            .createdAt(dto.getCreatedAt())
            .description(dto.getDescription())
            .experience(dto.getExperience())
            .quantity(dto.getQuantity())
            .rankAt(dto.getRankAt())
            .salary(dto.getSalary())
            .title(dto.getTitle())
            .type(dto.getType())
            .view(dto.getView())
            .status(dto.getStatus())
            .deadline(dto.getDeadline()) 
            .category(dto.getCategory())
            .build();
    }

    public RecruitmentDto toDto(RecruitmentEntity entity){
        return RecruitmentDto.builder()
        .id(entity.getId())
        .address(entity.getAddress())
        .description(entity.getDescription())
        .createdAt(entity.getCreatedAt())
        .experience(entity.getExperience())
        .rankAt(entity.getRankAt())
        .salary(entity.getSalary())
        .type(entity.getType())
        .title(entity.getTitle())
        .quantity(entity.getQuantity())
        .deadline(entity.getDeadline()) 
        .view(entity.getView())
        .status(entity.isStatus())
        .company(entity.getCompanyEntity())
        .category(entity.getCategory())
        .build();
    }


    public RecruitmentDto toDtoMissionField(RecruitmentEntity entity){
        return RecruitmentDto.builder()
        .id(entity.getId())
        .description(entity.getDescription())
        .type(entity.getType())
        .title(entity.getTitle())
        .quantity(entity.getQuantity())
        .company(entity.getCompanyEntity())
        .category(entity.getCategory())
        .build();
    }

    public List<RecruitmentDto> getListRecruitmentDto(List<RecruitmentEntity> recruitmentEntities){
        return recruitmentEntities.stream().map(entity -> toDtoMissionField(entity)).collect(Collectors.toList());
    }

    public List<RecruitmentEntity> getListRecruitmentEntity(List<RecruitmentDto> recruitmentsDto){
        return recruitmentsDto.stream().map(entity -> toEntity(entity)).toList();
    }


    

}
