package com.woking.demo.dto;

import java.util.Date;

import lombok.Builder;

@Builder
public record ApplyjobDto (
     Integer id,
     Date created,
     String nameCv,
     boolean status,
     String text,
     int recruitmentId,
     int userId
)
{  }