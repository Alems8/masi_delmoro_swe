/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainLogic;


import BusinessLogic.RequestManager;
import ObserverUtil.Subject;

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
        rm.displayUserBookings(this);
    }
    
    public void blindBook(Sport sport, String clb, String date, int hour){
        rm.requestBlindBooking(sport, clb, date, hour, this);
    }
    
    public void viewBlindBookings(Sport sport){
        rm.displayBlindBookings(sport);
    }
    
    public void bookSpot(int key){
        rm.requestSpot(this, key);
    }
    
    public void addFavouriteClub(String club){
        rm.addFavouriteClub(this, club);
    }
    
    public void addMatchResult(String [] users, int key){
        ArrayList<String> winners = new ArrayList<>(Arrays.asList(users));
        rm.addMatchResult(winners, key);
    }
    public void viewRecord(){
        rm.displayUserRecord(this);
    }
    
    public void deleteAccount() {
        rm.deleteUser(this);
    }
}
