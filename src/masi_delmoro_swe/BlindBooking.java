/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author thomas
 */
public class BlindBooking extends Booking {
    private ArrayList<User> users;
    
    public BlindBooking(Club club, Field field, LocalDate date, Integer hour, User user) {
        super(club, field, date, hour);
        this.users.add(user);
    }
    
    public boolean addUser(User user){
        if(users.size() < this.field.sport.numPlayers){
            users.add(user);
            return true;
        }
        System.out.println("Non ci sono più posti disponibili");
        return false;
    }
}
