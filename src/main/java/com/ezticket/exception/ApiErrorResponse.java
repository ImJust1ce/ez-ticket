package com.ezticket.exception;

import com.ezticket.dto.response.Meta;

import java.util.List;

public record ApiErrorResponse(ErrorBody error, Meta meta) {

    public record ErrorBody(String code, String message, List<String> details) {
    }
}
