package com.example.observers;

import com.example.Service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final static Logger logger = LoggerFactory.getLogger(Observable.class);

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

    public void restartEventProcessing(IObserver observer){
        observer.restartEventProcessing();
    }

    public synchronized void notifyObservers(String eventId, String topicName){
        for (IObserver observer : observers){
            if(!observer.isProcessingEvent() && observer.getSubscribedTopicName().equalsIgnoreCase(topicName)){
                observer.relayEvent(eventId);
            }
        }
    }

}
