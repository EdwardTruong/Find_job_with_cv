package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.RoleDto;
import com.example.demo.dto.UserDto;

public interface RoleService  {
    public List<RoleDto> findAllRole();

    boolean roleExisted(Integer id);
 
    public boolean isCompany(UserDto dto);

}
