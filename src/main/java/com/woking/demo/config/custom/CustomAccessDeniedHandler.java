package com.woking.demo.config.custom;

import java.io.IOException;


import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;


/*
 * Chưa đụng tới 
 * Dùng để điều hướng về lỗi 404 nếu đăng nhập thành công
 */

@Component
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    public void handle(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            response.sendRedirect("errors/error-404"); // Chuyển hướng đến file 404.html
        } else {
            super.handle(request, response, accessDeniedException); // Xử lý lỗi mặc định
        }
    }
}

