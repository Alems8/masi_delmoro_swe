package BalanceMonitor;

import java.util.ArrayList;

public abstract class Subject {

    private ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void notifyObservers(Object obj){
        for(Observer o : observers)
            o.update(obj, this);
    }
}
