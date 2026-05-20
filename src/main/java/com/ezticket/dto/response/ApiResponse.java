package com.ezticket.dto.response;

public record ApiResponse<T>(T data, Meta meta) {

    public static <T> ApiResponse<T> of(T data, String requestId) {
        return new ApiResponse<>(data, new Meta(requestId));
    }
}
