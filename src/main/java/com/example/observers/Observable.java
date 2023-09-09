package com.example.observers;

import java.util.List;

public class Observable {

    private List<IObserver> observers;

    private static Observable observable = null;

    private Observable(){}

    public static synchronized Observable getObservable(){
        if(observable == null){
            observable = new Observable();
        }
        return observable;
    }

    public void subscribe(IObserver observer){
        observers.add(observer);
    }

    public synchronized void notifyObservers(String eventId, String topicName){
        for (IObserver observer : observers){
            if(!observer.isProcessingEvent() && observer.getSubscribedTopicName().equalsIgnoreCase(topicName)){
                observer.relayEvent(eventId);
            }
        }
    }

}
