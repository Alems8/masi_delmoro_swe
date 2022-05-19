package ObserverUtil;

import java.util.ArrayList;

public abstract class Subject {

    private final ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void notifyObservers(Object obj){
        for(Observer o : observers)
            o.update(obj, this);
    }
}
