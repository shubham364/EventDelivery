package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Consumers {

    @JsonProperty("_id")
    private String consumerId;

    private String topicName;

    private String cursor;

    private int maxRetry;

    private int currRetry;

    private boolean processingEvent;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public int getCurrRetry() {
        return currRetry;
    }

    public void setCurrRetry(int currRetry) {
        this.currRetry = currRetry;
    }

    public boolean isProcessingEvent() {
        return processingEvent;
    }

    public void setProcessingEvent(boolean processingEvent) {
        this.processingEvent = processingEvent;
    }

    @Override
    public String toString() {
        return "Consumers{" +
                "consumerId='" + consumerId + '\'' +
                ", topicName='" + topicName + '\'' +
                ", cursor='" + cursor + '\'' +
                ", maxRetry=" + maxRetry +
                ", currRetry=" + currRetry +
                ", processingEvent=" + processingEvent +
                '}';
    }
}
