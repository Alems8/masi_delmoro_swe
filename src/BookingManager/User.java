/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookingManager;


import BalanceMonitor.BalanceMonitor;
import BalanceMonitor.Subject;
import Club.Club;
import Sport.Sport;
import Person.Person;

import java.util.ArrayList;

/**
 *
 * @author Alessio
 */
public class User implements Subject {
    public String username;
    private Person person;
    private BookingManager bm;
    private int balance = 0;
    private ArrayList<Club> favouriteClubs = new ArrayList<>();
    public int[] record = new int[2];
    private BalanceMonitor monitor;
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
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<Club> getFavouriteClubs() {
        return this.favouriteClubs;
    }
    
    public void joinClub(String club) {
        //Pagamento costo associazione
        bm.requestJoinClub(this, club);
    }
    
    public void bookField(Sport sport, String clb, String date, int hour, String player2, String player3, String player4) throws LowBalanceException {
        ArrayList<String> players = new ArrayList<>();
        players.add(this.username);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        bm.requestBooking(sport, clb, date, hour, players);
        notifyChanges();
    }

    public void bookField(Sport sport, String clb, String date, int hour, String player2, String player3,
                             String player4, String player5, String player6, String player7, String player8,
                             String player9, String player10) {
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
        bm.requestBooking(sport, clb, date, hour, players);
        notifyChanges();
    }
    
    public void addFunds(int money){
        bm.rechargeAccount(this, money);
    }
    
    public void deleteBooking(int id){
        bm.deleteUserBooking(this, id);
    }
    
    public void viewBookings(){
        bm.displayUserBookings(this);
    }
    
    public void blindBook(Sport sport, String clb, String date, int hour){
        bm.requestBlindBooking(sport, clb, date, hour, this);
        notifyChanges();
    }
    
    public void viewBlindBookings(){
        bm.displayBlindBookings();
    }
    
    public void bookSpot(int key){
        bm.requestSpot(this, key);
        notifyChanges();
    }
    
    public void addFavouriteClub(String club){
        bm.addUserFavouriteClub(this, club);
    }
    
    public void addMatchResult(String winner1, String winner2, int key){
        ArrayList<String> winners = new ArrayList<>();
        winners.add(winner1);
        winners.add(winner2);
        bm.addResult(winners, key);
    }

    public void addMatchResult(String winner1, String winner2, String winner3, String winner4, String winner5, int key){
        ArrayList<String> winners = new ArrayList<>();
        winners.add(winner1);
        winners.add(winner2);
        winners.add(winner3);
        winners.add(winner4);
        winners.add(winner5);
        bm.addResult(winners, key);
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
    
    public void deleteAccount() {
        try{bm.deleteUser(this);}
        catch(PendingBookingException e) {
            System.out.println("Hai delle prenotazioni in sospeso");
        }
    }
}
