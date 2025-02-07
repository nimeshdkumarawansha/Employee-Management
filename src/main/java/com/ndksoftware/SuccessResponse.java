package com.ndksoftware;

public class SuccessResponse {
    private final String message;
    private final Long id;
    private final long timestamp;

    public SuccessResponse(String message, Long id) {
        this.message = message;
        this.id = id;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }
}