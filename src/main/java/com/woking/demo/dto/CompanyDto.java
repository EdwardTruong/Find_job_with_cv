package com.woking.demo.dto;


import java.util.List;

import lombok.Builder;

@Builder
public record CompanyDto (
    Integer id,
    String email,
    String name,
    String address,
    String phoneNumber,
    String description,
    String logo,
    Boolean status,
    Integer userId,
    List<RecruitmentDto> recruitments,
    UserDto user)
    {}
