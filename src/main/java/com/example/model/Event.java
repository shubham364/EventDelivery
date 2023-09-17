package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {

    @JsonProperty("_id")
    private String id;

    private String userId;

    private String payload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
