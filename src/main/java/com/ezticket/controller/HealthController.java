package com.ezticket.controller;

import com.ezticket.config.RequestIdFilter;
import com.ezticket.dto.response.ApiResponse;
import com.ezticket.dto.response.HealthResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<HealthResponse> health(HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID_ATTR);
        HealthResponse body = new HealthResponse("UP", "ticketing-system");
        return ApiResponse.of(body, requestId);
    }
}
