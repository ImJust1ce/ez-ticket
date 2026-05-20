package com.ezticket.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminKeyInterceptor implements HandlerInterceptor {

    public static final String ADMIN_KEY_HEADER = "X-Admin-Key";

    @Value("${app.admin-key}")
    private String adminKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String provided = request.getHeader(ADMIN_KEY_HEADER);
        if (provided == null || !provided.equals(adminKey)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing admin key");
            return false;
        }
        return true;
    }
}
