package com.woking.demo.service;

import java.util.List;

import com.woking.demo.dto.RoleDto;
import com.woking.demo.dto.UserDto;

public interface RoleService  {
    public List<RoleDto> findAllRole();

    boolean roleExisted(Integer id);
 
    public boolean isCompany(UserDto dto);

}
