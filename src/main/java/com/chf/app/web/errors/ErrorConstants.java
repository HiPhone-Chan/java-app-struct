package com.chf.app.web.errors;

import java.net.URI;

public class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://localhost/problem";

    public static final URI DEFAULT_TYPE = craeteURI("/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = craeteURI("/constraint-violation");
    public static final URI PARAMETERIZED_TYPE = craeteURI("/parameterized");
    public static final URI ENTITY_NOT_FOUND_TYPE = craeteURI("/entity-not-found");
    public static final URI INVALID_PASSWORD_TYPE = craeteURI("/invalid-password");
    public static final URI LOGIN_ALREADY_USED_TYPE = craeteURI("/login-already-used");

    private ErrorConstants() {
    }

    private static URI craeteURI(String str) {
        return URI.create(PROBLEM_BASE_URL + str);
    }
}
