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
    private Person person;
    private final UserInterface ui; //TODO FIXME
    private int balance = 0;
    private final ArrayList<UserClub> favouriteClubs = new ArrayList<>(); //TODO FIXME
    public Map<Sport, int[]> record = new HashMap();



    public User(String username, Person person, UserInterface ui, BalanceMonitor balanceMonitor) {
        this.username = username;
        this.person = person;
        this.ui = ui;
        addObserver(balanceMonitor);
    }



    public int getBalance() {
        return this.balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
        notifyObservers(this.balance);
    }


    public void addFavouriteClub(String club){
        ui.addUserFavouriteClub(this, club);
    }
    public ArrayList<UserClub> getFavouriteClubs() {
        return this.favouriteClubs;
    }
    public void joinClub(String club) {
        ui.requestJoinClub(this, club);
    }



    public void bookField(Sport sport, String clb, String date, int hour, String player2, String player3, String player4) throws LowBalanceException {
        ArrayList<String> players = new ArrayList<>();
        players.add(this.username);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        ui.requestBooking(sport, clb, date, hour, players);
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
        ui.requestBooking(sport, clb, date, hour, players);
    }

    public void deleteBooking(int id){
        ui.deleteUserBooking(this, id);
    }
    public void viewBookings(){
        ui.displayUserBookings(this);
    }



    public void blindBook(Sport sport, String clb, String date, int hour){
        ui.requestBlindBooking(sport, clb, date, hour, this);
    }
    public void viewBlindBookings(){
        ui.displayBlindBookings();
    }
    public void bookSpot(int key){
        ui.requestSpot(this, key);
    }



    
    public void addMatchResult(String winner1, String winner2, int key){
        ArrayList<String> winners = new ArrayList<>();
        winners.add(winner1);
        winners.add(winner2);
        try{ui.addResult(winners, key);}
        catch(WrongNameException e) {
            System.out.println("I nomi inseriti non sono corretti");
        }
    }
    public void addMatchResult(String winner1, String winner2, String winner3, String winner4, String winner5, int key){
        ArrayList<String> winners = new ArrayList<>();
        winners.add(winner1);
        winners.add(winner2);
        winners.add(winner3);
        winners.add(winner4);
        winners.add(winner5);
        try{ui.addResult(winners, key);}
        catch(WrongNameException e) {
            System.out.println("I nomi inseriti non sono corretti");
        }
    }



    public void addFunds(int money){
        ui.rechargeAccount(this, money);
    }
    public void viewRecord(){
        ui.displayUserRecord(this);
    }
    public void deleteAccount() {
        try{ui.deleteUser(this);}
        catch(PendingBookingException e) {
            System.out.println("Hai delle prenotazioni in sospeso");
        }
    }
}
