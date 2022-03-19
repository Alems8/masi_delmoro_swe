/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

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
    private float gains;
    private ArrayList<User> members = new ArrayList<>();
    int opening;
    int closure;
    public ArrayList<Integer> times = new ArrayList<>(); //MODIFICATO

    public Club(String name, int price, int opening, int closure) { //MODIFICATO
        this.name = name;
        this.price = price;
        this.opening = opening;
        this.closure = closure;
        for(int i=opening; i<closure; i++){
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
    
    
    public boolean getRequest(User user) {
        if(isMember(user)) {
            return false;
        }
        members.add(user);
        return true;
    }
   
    public void subscribe(BookingManager bm) {
        bm.addClub(this);
    }
    
    public void addField(String name) {
        fields.add(new Field(name, times));
    }
}
