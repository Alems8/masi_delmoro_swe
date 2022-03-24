/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alessio
 */
public class User {
    public String username;
    private String name;
    private String surname;
    private String email;
    private BookingManager bm;
    private int balance = 0;
    ArrayList<Club> favouriteClubs = new ArrayList<>();
    public int[] record = new int[2];
    //private Map<Integer,Booking> bookings = new HashMap();

    public User(String username, Person person, BookingManager bm) {
        this.username = username;
        this.name = person.name;
        this.surname = person.surname;
        this.email = person.email;
        this.bm = bm;
        this.record[0] = 0;
        this.record[1] = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    public boolean joinClub(String club) {
        //Pagamento costo associazione
        return bm.requestJoinClub(this, club);
    }
    
    public boolean bookField(String clb, String date, int hour, String user2, String user3, String user4) {
       return bm.requestBooking(clb, date, hour,this, user2, user3, user4);
       //bookings.put(bookings.size()+1,booking);
    }
    
    public void addFunds(int money){
        bm.rechargeAccount(this, money);
    }
    
    public void deleteBooking(int key){
        //Booking booking = bookings.remove(key);
        bm.cancelBooking(key);
    }
    
    public void viewBookings(){
        bm.displayUserBookings(this);
    }
    
    public boolean blindBook(String clb, String date, int hour){
        return bm.requestBlindBooking(clb, date, hour, this);
    }
    
    public void viewBlindBookings(){
        bm.displayBlindBookings();
    }
    
    public boolean bookSpot(int key){
        return bm.requestSpotBooking(key, this);
    }
    
    public void deleteSpot(int key){
        bm.cancelSpot(key, this);
    }
    
    public boolean addFavouriteClub(String club){
        return bm.addUserFavouriteClub(this, club);
    }
    
    public void addMatchResult(String winner1, String winner2, int key){
        bm.addResult(winner1, winner2, key);
    }
    
    public void viewRecord(){
        bm.displayUserRecord(this);
    }
}
