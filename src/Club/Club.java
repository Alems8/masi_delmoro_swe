/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Club;

import BookingManager.BookingManager;
import Sport.Sport;
import BookingManager.User;

import java.util.ArrayList;

/**
 *
 * @author Alessio
 */
public class Club {
    public String name;
    public ArrayList<Field> fields = new ArrayList<>();
    public int price;
    public int memberPrice;
    public int joinClubPrice;
    private ArrayList<User> members = new ArrayList<>();
    int opening;
    int closure;
    public ArrayList<Integer> times = new ArrayList<>(); //MODIFICATO

    public Club(String name, int price, int memberPrice,int opening, int closure, int joinClubPrice) { //MODIFICATO
        this.name = name;
        this.price = price;
        this.opening = opening;
        this.joinClubPrice = joinClubPrice;
        this.closure = closure;
        this.memberPrice = memberPrice;
        int fakeClosure = closure;
        if(closure < opening)
            fakeClosure = closure + 24;
        for(int i=opening; i<fakeClosure; i++){ //FIX ME
            if(i>=24)
                times.add(i-24);
            else
                times.add(i);
        }
    }
    
    
    public boolean isMember(User user) {
        for (User member : members) {
            if(member == user)
                return true;
        }
        return false;
    }
    
    public void addMember(User user){
        members.add(user);
    }
   
    public void subscribe(BookingManager bm) {
        bm.addClub(this);
    }
    
    public void addField(String name, Sport sport) {
        fields.add(new Field(name, sport, new ArrayList<>(times)));
    }
}
