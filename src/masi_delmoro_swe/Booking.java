/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author aleal
 */
public class Booking {
    private ArrayList<User> players;
    private Club club;
    private Field field;
    private LocalDate date;
    private Integer hour;

    public Booking(Club club, Field field, LocalDate date, Integer hour, ArrayList<User> users) {
        this.club = club;
        this.field = field;
        this.date = date;
        this.hour = hour;
        this.players = new ArrayList<>(users);
        
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public Club getClub() {
        return club;
    }

    public Field getField() {
        return field;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getHour() {
        return hour;
    }
    
    
    
}
