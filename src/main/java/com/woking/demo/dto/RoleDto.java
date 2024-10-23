package com.woking.demo.dto;

import lombok.Builder;

@Builder
public record RoleDto(

    Integer id,
    String name
) {}

