package com.diplom.web_service_attendance.configSecurity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        String redirectUrl = "/";

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_TEACHER")) {
                redirectUrl = "/app/teacher/groups";
                break;
            } else if (authority.getAuthority().equals("ROLE_MONITOR")) {
                redirectUrl = "/app/monitor/lessons";
                break;
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/app/admin/main";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
