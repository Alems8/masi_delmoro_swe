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
    
    public BlindBooking(Club club, Field field, LocalDate date, int hour, ArrayList<User> players) {
        super(club, field, date, hour, players);
    }
    
    public boolean addPlayer(User user){
        if(players.size() < this.field.sport.numPlayers){
            players.add(user);
            return true;
        }
        System.out.println("Non ci sono più posti disponibili");
        return false;
    }
    
    public boolean removePlayer(User user){
        if(players.isEmpty())
            return false;
        players.remove(user);
        return true;
    }
}
