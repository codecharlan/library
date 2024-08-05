package com.casava.library.advice;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum InternalCode {
    CASAVA_LIB_001(1, "Request performed successfully", null),
    CASAVA_LIB_002(2, "Internal server error", INTERNAL_SERVER_ERROR),
    CASAVA_LIB_003(3, "The resource was not found", NOT_FOUND),
    CASAVA_LIB_004(4, "Bad request, client error calling the request", BAD_REQUEST),
    CASAVA_LIB_005(5, "Unauthorized, requires user authentication", UNAUTHORIZED),
    CASAVA_LIB_006(6, "Forbidden, implies that the request is understood by the server, but still refuses to fulfill it", FORBIDDEN),
    CASAVA_LIB_007(7, "Resource not implemented.", NOT_IMPLEMENTED),
    CASAVA_LIB_008(8, "Resource already exists.", CONFLICT);

    private final String codeDescription;
    private final String codeNumber;
    private final HttpStatus httpStatus;

    InternalCode(int codeNumber, String codeDescription, HttpStatus status) {
        this.codeNumber = String.format("%03d", codeNumber);
        this.codeDescription = codeDescription;
        this.httpStatus = status;
    }

}

