package com.woking.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.woking.demo.dao.RoleDao;
import com.woking.demo.dto.RoleDto;
import com.woking.demo.dto.UserDto;
import com.woking.demo.entity.RoleEntity;
import com.woking.demo.mapper.RoleMapper;

@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    RoleDao roleDao;

    @Autowired
    RoleMapper roleMapper;
    
    @Override
    public List<RoleDto> findAllRole() {
        List<RoleEntity> listRoleEntity = roleDao.findAll();
        return roleMapper.roleListEntityToDto(listRoleEntity);
    }
    
    @Override
    public boolean roleExisted(Integer id) {
        Optional<RoleEntity> resutl = roleDao.findById(id);
        return resutl.isPresent();
    }


    @Override
    public boolean isCompany(UserDto dto){
		return dto.getIdCompany() != 0;
	}
    
	public List<RoleEntity> convertAuthoritiesToRoles(List<GrantedAuthority> authorities) {
	    return authorities.stream()
	            .map(authority -> {
	                RoleEntity roleEntity = new RoleEntity();
	                roleEntity.setName(authority.getAuthority());
	                return roleEntity;
	            })
	            .collect(Collectors.toList());
	}
}
