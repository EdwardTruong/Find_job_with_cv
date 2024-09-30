package com.example.demo.mapper;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.RoleDto;
import com.example.demo.entity.RoleEntity;




@Component
public class RoleMapper {
    public RoleDto roleEntityToDto(RoleEntity role) {
		return RoleDto.builder()
				.id(role.getId())
				.name(role.getName())
				.build();
	}
	
	public List<RoleDto> roleListEntityToDto(List<RoleEntity> entities) {
		return entities.stream()
				.map(entity -> roleEntityToDto(entity))
				.toList();
	}
		
	public RoleEntity toEntity(RoleDto dto) {
	  	return RoleEntity.builder()
	  			.id(dto.id())
	  			.name(dto.name())
	  			.build();
	  }
	
	public List<RoleEntity> roleListDtoToEntity(List<RoleDto> dto) {
		return dto.stream()
				.map(entities -> toEntity(entities))
				.toList();
	}
}
