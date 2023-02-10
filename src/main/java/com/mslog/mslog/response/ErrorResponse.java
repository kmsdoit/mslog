package com.mslog.mslog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code" : "400",
 *     "meesage" : "잘못된 요청입니다",
 *     "validation" : {
 *         "title" : "값을 입력해주세요"
 *     }
 *
 * }
 * */

@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 값이 비어있는 객체가 있다면 응답값에서 삭제되어서 나옴
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName,errorMessage);
    }

}
