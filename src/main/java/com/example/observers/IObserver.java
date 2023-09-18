package com.example.observers;

public interface IObserver {

    boolean isProcessingEvent();

    String getSubscribedTopicName();

    void relayEvent(String eventId);

    void restartEventProcessing();

    String getIdentifier();

}
