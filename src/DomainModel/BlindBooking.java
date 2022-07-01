/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainModel;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author thomas
 */
public class BlindBooking extends Booking {

    private boolean full = false;

    public BlindBooking(UserClub club, Field field, LocalDate date, int hour, User user) {
        super(club, field, date, hour, new ArrayList<>(){{add(user);}} );
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public void addPlayer(User user){
        if(!isFull()){
            players.add(user);
            if(players.size() == field.sport.numPlayers)
                setFull(true);
        }
        else
            System.out.println("Non ci sono più posti disponibili");
    }
    
    public void removePlayer(User user){
        players.remove(user);
        setFull(false);
    }
}
