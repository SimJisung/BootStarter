package com.pirate.commons;

import lombok.Data;

import java.util.List;

/**
 * Created by simjisung on 15. 9. 16..
 */
@Data
public class ErrorResponse {
    private String message;
    private String code;
    private List<FieldError> errors;


    @Data
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }

}
