/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author aleal
 */
public class Booking {
    protected UserClub club;
    protected Field field;
    protected LocalDate date;
    protected int hour;
    protected ArrayList<User> players;
    protected final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Booking(UserClub club, Field field, LocalDate date, int hour, ArrayList<User> players) {
        this.club = club;
        this.field = field;
        this.date = date;
        this.hour = hour;
        this.players = players;
    }

    public UserClub getClub() {
        return club;
    }

    public Field getField() {
        return field;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }
    
    public ArrayList<User> getPlayers(){
        return players;
    }
    
    public boolean containsUser(User user){
        return this.players.contains(user);
    }
   
    @Override
    public String toString() {
        return ")" + this.club.getClub().name + " - " + this.field.name + " - " + this.date.format(dtf) + "  "
                +this.hour ;
    }
}
