package com.chf.app.web.errors;

public class ErrorVM {

    private final String code;

    private final String description;

    public ErrorVM() {
        this(null);
    }

    public ErrorVM(String message) {
        this(message, null);
    }

    public ErrorVM(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ErrorVM [code=" + code + ", description=" + description + "]";
    }

}
