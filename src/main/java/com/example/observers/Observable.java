package com.example.observers;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private static List<IObserver> observers;

    private static Observable observable = null;

    private Observable(){}

    public static synchronized Observable getObservable(){
        if(observable == null){
            observable = new Observable();
        }
        return observable;
    }

    public void subscribe(IObserver observer){
        if(observers == null){
            observers = new ArrayList<>();
        }
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
