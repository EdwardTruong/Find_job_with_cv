package com.woking.demo.dto;

import lombok.Builder;

@Builder
public record CvDto(
    Integer id,
    String filename
) {
    
}

