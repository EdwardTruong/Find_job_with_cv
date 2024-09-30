package com.example.demo.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.CvDto;
import com.example.demo.entity.CvEntity;

@Component
public class CvMapper {

    public CvEntity cvDtoToEntity(CvDto dto){

        if(dto.id() != null){
            return CvEntity.builder()
            .id(dto.id())
            .name(dto.filename())
            .build();
        }

        return CvEntity.builder()
            .name(dto.filename())
            .build();
    }

    public List<CvEntity> listCvDtoToEntity(List<CvDto> cvesDto){
        return cvesDto.stream().map(entity-> cvDtoToEntity(entity)).toList();
    }

    public CvDto cvEntityToDto(CvEntity entity){
        return CvDto.builder()
            .id(entity.getId())
            .filename(entity.getName())
            .build();
    }

    public List<CvDto> toListDto(List<CvEntity> cvesEntity){
        return cvesEntity.stream().map(dto->cvEntityToDto(dto)).toList();
    }
}
