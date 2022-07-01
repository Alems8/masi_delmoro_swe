/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainModel;

import BusinessLogic.RequestManager;
import ObserverUtil.Subject;

import java.util.ArrayList;

/**
 *
 * @author Alessio
 */
public class Club extends Subject {
    public String name;
    public ArrayList<Field> fields = new ArrayList<>();
    int opening;
    int closure;
    public int memberDiscount;
    public ArrayList<Integer> times = new ArrayList<>();
    private final ArrayList<Person> members = new ArrayList<>();

    public Club(String name, int opening, int closure, int memberDiscount) {
        this.name = name;
        this.memberDiscount = memberDiscount;
        this.opening = opening;
        this.closure = closure;
        int fakeClosure = closure;
        if(closure < opening)
            fakeClosure = closure + 24;
        for(int i=opening; i<fakeClosure; i++){
            if(i>=24)
                times.add(i-24);
            else
                times.add(i);
        }
    }

    public UserClub subscribe(RequestManager rm, int joinClubPrice) {
        return rm.addClub(this, joinClubPrice);
    }
    
    public void addField(String name, Sport sport, int price) {
        fields.add(new Field(name, sport, new ArrayList<>(times), price));
    }

    public void addMember(Person person){
        members.add(person);
        notifyObservers(person);
    }

    public Person getMember(int id){
        return members.get(id);
    }

    public int getMembersSize(){
        return members.size();
    }

    public boolean isMember(Person person){
        return members.contains(person);
    }

}
