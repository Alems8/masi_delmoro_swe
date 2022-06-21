/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;


import BusinessLogic.BookingChecker;
import BusinessLogic.PendingBookingException;
import BusinessLogic.RequestManager;
import Club.UserClub;
import ObserverUtil.Subject;
import Sport.Sport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alessio
 */
public class User extends Subject {
    public String username;
    private final Person person;
    private final RequestManager rm;
    private int balance = 0;
    private final ArrayList<UserClub> favouriteClubs = new ArrayList<>();
    public Map<Sport, int[]> record = new HashMap();
    BookingChecker bm;

    public User(String username, Person person, RequestManager rm) {
        this.username = username;
        this.person = person;
        this.rm = rm;
    }

    public Person getPerson() {
        return this.person;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        notifyObservers(this.balance);
    }

    public ArrayList<UserClub> getFavouriteClubs() {
        return this.favouriteClubs;
    }
    
    public void joinClub(String club) {
        rm.requestJoinClub(this, club);
    }
    
    public void bookField(Sport sport, String clb, String date, int hour, String [] users){
        ArrayList<String> players = new ArrayList<>(Arrays.asList(users));
        players.add(this.username);
        rm.requestBooking(sport, clb, date, hour, players);
    }

    
    public void addFunds(int money){
        rm.topUpBalance(this, money);
    }
    
    public void deleteBooking(int id){
        rm.deleteUserBooking(this, id);
    }
    
    public void viewBookings(){
        bm.displayUserBookings(this);
    }
    
    public void blindBook(Sport sport, String clb, String date, int hour){
        rm.requestBlindBooking(sport, clb, date, hour, this);
    }
    
    public void viewBlindBookings(Sport sport){
        bm.displayBlindBookings(sport);
    }
    
    public void bookSpot(int key){
        rm.requestSpot(this, key);
    }
    
    public void addFavouriteClub(String club){
        bm.addFavouriteClub(this, club);
    }
    
    public void addMatchResult(String winner1, String winner2, int key){
        ArrayList<String> winners = new ArrayList<>();
        winners.add(winner1);
        winners.add(winner2);
        bm.addMatchResult(winners, key);
    }

    public void addMatchResult(String winner1, String winner2, String winner3, String winner4, String winner5, int key){
        ArrayList<String> winners = new ArrayList<>();
        winners.add(winner1);
        winners.add(winner2);
        winners.add(winner3);
        winners.add(winner4);
        winners.add(winner5);
        bm.addMatchResult(winners, key);
    }
    public void viewRecord(){
        bm.displayUserRecord(this);
    }
    
    public void deleteAccount() {
        try{bm.deleteUser(this);}
        catch(PendingBookingException e) {
            System.out.println("Hai delle prenotazioni in sospeso");
        }
    }
}
