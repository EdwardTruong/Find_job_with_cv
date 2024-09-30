package com.example.demo.dto;



import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.CompanyEntity;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentDto{
    
    Integer id;
    
    @NotBlank(message = "Yêu cầu địa chỉ.")
    private String address;

    private Date createdAt;

    @Temporal(value = TemporalType.DATE)
    @NotNull(message = "Yêu cầu có ngày kết thúc.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    @NotBlank(message = "Yêu cầu thông tin miêu tả.")
    private String description;

    @NotBlank(message =  "Yêu cầu cần thông tin về kinh nghiệm.")
    private String experience;

    @NotNull
    @Min(value = 1, message = "Giá trị phải lớn hơn hoặc bằng 1")
    private int quantity;

    private String rankAt;

    @Size(min = 10, message = "Yêu cầu điền thông tin thu nhập, thấp nhất là 10,000 VND.")
    private String salary;

    @NotBlank(message = "Yêu cầu điền thông tin tiêu đề.")
    private String title;

    @NotNull(message = "Yêu cầu có thông tin hình thức làm việc.")
    private String type;

    private Integer view;

    private Boolean status;
    
    CompanyEntity company;

    CategoryEntity category;
}
   

    

