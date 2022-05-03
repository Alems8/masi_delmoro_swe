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
public class User implements Subject {
    public String username;
    private Person person;
    private BookingManager bm;
    private int balance = 0;
    ArrayList<Club> favouriteClubs = new ArrayList<>();
    public int[] record = new int[2];
    BalanceMonitor monitor;
    //private Map<Integer,Booking> bookings = new HashMap();

    public User(String username, Person person, BalanceMonitor monitor, BookingManager bm) {
        this.username = username;
        this.person = person;
        this.bm = bm;
        this.monitor = monitor;
        subscribe();
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
    
    public boolean bookField(Sport sport, String clb, String date, int hour, String player2, String player3, String player4) {
        ArrayList<String> players = new ArrayList<String>();
        players.add(this.username);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        boolean result = bm.requestBooking(sport, clb, date, hour, players);
       notifyChanges();
       return result;
    }

    public boolean bookField(Sport sport, String clb, String date, int hour, String player2, String player3, String player4,
                             String player5, String player6, String player7, String player8, String player9, String player10) {
        ArrayList<String> players = new ArrayList<String>();
        players.add(this.username);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);
        players.add(player7);
        players.add(player8);
        players.add(player9);
        players.add(player10);
        boolean result = bm.requestBooking(sport, clb, date, hour, players);
        notifyChanges();
        return result;
    }
    
    public void addFunds(int money){
        bm.rechargeAccount(this, money);
    }
    
    public boolean deleteBooking(int id){

        return bm.deleteUserBooking(this, id);
    }
    
    public void viewBookings(){
        bm.displayUserBookings(this);
    }
    
    public boolean blindBook(Sport sport, String clb, String date, int hour){
        boolean result = bm.requestBlindBooking(sport, clb, date, hour, this);
        notifyChanges();
        return result;
    }
    
    public void viewBlindBookings(){
        bm.displayBlindBookings();
    }
    
    public boolean bookSpot(int key){
        boolean result = bm.requestSpot(key, this);
        notifyChanges();
        return result;
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

    @Override
    public void subscribe() {
        monitor.attach(this);
    }

    @Override
    public void unsubscribe() {
        monitor.detach(this);
    }

    @Override
    public void notifyChanges() {
        monitor.update();
    }
    
    public void deleteAccount() throws PendingBookingException {
        bm.deleteUser(this);
    }
}
