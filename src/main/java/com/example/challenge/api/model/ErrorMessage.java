package com.example.challenge.api.model;

import lombok.Generated;

@Generated
public class ErrorMessage {
    private Integer code;
    private String message;
    private String level;
    private String description;
    public Integer getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
