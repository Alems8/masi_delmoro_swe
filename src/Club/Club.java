/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Club;

import BookingManager.BookingManager;
import Sport.Sport;
import BookingManager.UserClub;

import java.util.ArrayList;

/**
 *
 * @author Alessio
 */
public class Club {
    public String name;
    public ArrayList<Field> fields = new ArrayList<>();
    public int price;
    int opening;
    int closure;
    public ArrayList<Integer> times = new ArrayList<>();

    public Club(String name, int price, int opening, int closure) {
        this.name = name;
        this.price = price;
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

    public UserClub subscribe(BookingManager bm, int memberPrice, int joinClubPrice) {
        return bm.addClub(this, memberPrice, joinClubPrice);
    }
    
    public void addField(String name, Sport sport) {
        fields.add(new Field(name, sport, new ArrayList<>(times)));
    }
}
