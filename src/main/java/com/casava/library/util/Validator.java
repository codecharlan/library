package com.casava.library.util;

import java.util.Objects;
import java.util.UUID;

public class Validator {
    public static void validateRequest(Object object) {
        if (!Objects.nonNull(object)) {
            throw new IllegalArgumentException("Request payload is required");
        }
    }
    public static void validateRequest(UUID id) {
        if (!Objects.nonNull(id)) {
            throw new IllegalArgumentException("Id is required");
        }
    }
}
