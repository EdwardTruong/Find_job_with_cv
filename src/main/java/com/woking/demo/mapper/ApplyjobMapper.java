package com.woking.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.woking.demo.dto.ApplyjobDto;
import com.woking.demo.entity.Applyjob;

@Component
public class ApplyjobMapper {
    
 

    public Applyjob toEntity(ApplyjobDto dto){
        

        return Applyjob.builder()
        .createAt(dto.created())
        .nameCv(dto.nameCv())
        .text(dto.text())
        .status(dto.status())

        .build();
    
    }

    public ApplyjobDto toDto(Applyjob entity){
        return ApplyjobDto.builder()
        .id(entity.getId())
        .nameCv(entity.getNameCv())
        .status(entity.isStatus())
        .build();
    }


    public List<Applyjob> getListApplyjob(List<ApplyjobDto> listDto){
    return listDto.stream().map(dto->toEntity(dto)).collect(Collectors.toList());
    }

    public List<ApplyjobDto> getListApplyjobDto (List<Applyjob> listEntity){
        return listEntity.stream().map(dto -> toDto(dto)).collect(Collectors.toList());
    }
}
