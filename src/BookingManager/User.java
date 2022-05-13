/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookingManager;


import BalanceMonitor.BalanceMonitor;
import BalanceMonitor.Subject;
import Sport.Sport;
import Person.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alessio
 */
public class User extends Subject {
    public String username;
    private final Person person;
    private final AbstractBookingManager bm;
    private int balance = 0;
    private final ArrayList<UserClub> favouriteClubs = new ArrayList<>();
    public Map<Sport, int[]> record = new HashMap();

    public User(String username, Person person, AbstractBookingManager bm, BalanceMonitor balanceMonitor) {
        this.username = username;
        this.person = person;
        this.bm = bm;
        addObserver(balanceMonitor);
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
        bm.requestJoinClub(this, club);
    }
    
    public void bookField(Sport sport, String clb, String date, int hour, String player2, String player3, String player4){
        ArrayList<String> players = new ArrayList<>();
        players.add(this.username);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        bm.requestBooking(sport, clb, date, hour, players);
    }

    public void bookField(Sport sport, String clb, String date, int hour, String player2, String player3,
                             String player4, String player5, String player6, String player7, String player8,
                             String player9, String player10) {
        ArrayList<String> players = new ArrayList<>();
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
        bm.requestBooking(sport, clb, date, hour, players);
    }
    
    public void addFunds(int money){
        bm.topUpBalance(this, money);
    }
    
    public void deleteBooking(int id){
        bm.deleteUserBooking(this, id);
    }
    
    public void viewBookings(){
        bm.displayUserBookings(this);
    }
    
    public void blindBook(Sport sport, String clb, String date, int hour){
        bm.requestBlindBooking(sport, clb, date, hour, this);
    }
    
    public void viewBlindBookings(){
        bm.displayBlindBookings();
    }
    
    public void bookSpot(int key){
        bm.requestSpot(this, key);
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
