package com.woking.demo.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.woking.demo.dto.CompanyDto;
import com.woking.demo.entity.CompanyEntity;

import lombok.Getter;
import lombok.Setter;


@Component
@Getter
@Setter
public class CompanyMapper {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RecruitmentMapper rMapper;

    public CompanyDto toDto(CompanyEntity entity){ // Extention- Check all application when running because i change a lot. It was delete logo, description ect..
            return CompanyDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .email(entity.getEmail())
            .phoneNumber(entity.getPhoneNumber())
            .address(entity.getAddress())
            .description(entity.getDescription())
            .logo(entity.getLogo())
            .user(userMapper.toDto(entity.getUser()))
            .status(entity.isStatus())
           .build();
        }

    public CompanyDto toDtoGetDetails(CompanyEntity entity){
        return CompanyDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .email(entity.getEmail())
        .phoneNumber(entity.getPhoneNumber())
        .address(entity.getAddress())
        .description(entity.getDescription())
        .logo(entity.getLogo())
        .recruitments(rMapper.getListRecruitmentDto(entity.getRecruitmentListEntity()))
        .status(entity.isStatus())
       .build();
    }   
        
    public CompanyEntity toEntity(CompanyDto dto){
        if(dto.id() == null){
            return CompanyEntity.builder()
            .email(dto.email())
            .name(dto.name())
            .address(dto.address())
            .phoneNumber(dto.phoneNumber())
            .description(dto.description())
            .status(true)
            .build();
        }
        return CompanyEntity.builder()
        .id(dto.id())
        .email(dto.email())
        .name(dto.name())
        .address(dto.address())
        .phoneNumber(dto.phoneNumber())
        .description(dto.description())
        .status(true)
        .build();
        }
        public List<CompanyEntity> toListCompanyEntity(List<CompanyDto> listDot){
            return listDot.stream().map(entity->toEntity(entity)).collect(Collectors.toList());
        } 

        public List<CompanyDto> toListCompanyDto(List<CompanyEntity> listEntity){
            return listEntity.stream().map(dto->toDtoGetDetails(dto)).collect(Collectors.toList());
        } 

        
    	public Page<CompanyDto> toListDto(Page<CompanyEntity> listCompaniesEntity) {
    	    return listCompaniesEntity.map(this::toDto);
    	}
    }
