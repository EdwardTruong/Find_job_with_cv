package com.woking.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyInfoDto {

    @NotBlank(message = "Yêu cầu tên công ty.")
    String name;

    @Email(message = "Email không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
    String email;

    @NotBlank(message = "Nhập địa chỉ.")
    String address;

    @Pattern(regexp = "^\\d{3,12}", message = "Chỉ nhật số. Ít nhất 3 nhiều nhất là 15 số.")
    String phoneNumber;

    @NotBlank(message = "Yêu cầu ít thông tin mô tả công ty.")
    String description;

    String logo;
}
