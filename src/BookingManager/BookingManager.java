/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookingManager;


import BalanceMonitor.BalanceMonitor;
import Booking.Booking;
import Booking.PrivateBooking;
import Booking.BlindBooking;
import Club.Club;
import Club.Field;
import Person.Person;
import Sport.Sport;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;


/**
 *
 * @author Alessio
 */
public class BookingManager extends AbstractBookingManager {
    private BalanceMonitor monitor;
    
    public BookingManager(BalanceMonitor monitor) {
        this.monitor = monitor;
        this.bd = new BookingDatabase();
    }

    public User createUser(Person person, String username, BookingChecker bc){
        User user = new User(username, person, bc, monitor);
        bd.addUser(user);
        return user;
    }


    public void removeUser(User user){
        bd.removeUser(user);
        System.out.println("Utente rimosso correttamente");
    }

    public int getUsersSize() {
        return bd.getUsersSize();
    }

    private void pay(User user, UserClub club) {
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        user.setBalance(user.getBalance() - price);
    }

    private void holdField(Field field, LocalDate date, int hour){
        ArrayList<Integer> times = field.timeTable.get(date);
        int k = times.indexOf(hour);
        times.remove(k);
    }

    void topUpUserBalance(User user, int money){
        user.setBalance(user.getBalance() + money);
    }

    private void refund(User user, UserClub club){
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        rechargeAccount(user, price);
    }

    public Booking getBooking(int id) {
        return bd.getBooking(id);
    }

    void displayBookings(ArrayList<Integer> keys){
        for(int k : keys)
            System.out.println(k+bd.getBooking(k).toString());
    }

    public UserClub addClub(Club clb, int memberPrice, int joinClubPrice) {
        UserClub club = new UserClub(clb, memberPrice, joinClubPrice);
        bd.addClub(club);
        return club;
    }

    public int getClubsSize(){
        return bd.getClubsSize();
    }

    void addClubMember(User user, UserClub club){
        club.addMember(user);
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }

    public void addUserFavouriteClub(User user, UserClub club) {
        user.getFavouriteClubs().add(club);
    }
    
    public void addResult(Sport sport, ArrayList<User> players, ArrayList<String> winners) {
        for (User u : players){
            if(u.record.containsKey(sport))
                u.record.get(sport)[0]++;
            else {
                int[] result = new int[2];
                u.record.put(sport, result);
                u.record.get(sport)[0]++;
            }
            for(String w : winners){
                if(w.equals(u.username)){
                    u.record.get(sport)[1]++;
                    u.record.get(sport)[0]--;
                }
            }
        }
    }
    
    public void releaseField(int id){
        Booking booking = bd.getBooking(id);
        bd.removeBooking(id);
        booking.getField().timeTable.get(booking.getDate()).add(booking.getHour());
    }
    
    public void displayUserRecord(User user){
        System.out.println("Il tuo storico è: ");
        for(Sport k : user.record.keySet()){
            System.out.println(k.name + ": " + user.record.get(k)[1] + " vittorie - " + user.record.get(k)[0] +
                    " sconfitte");
        }
    }

    
    public void payJoinClub(User user, UserClub club) throws LowBalanceException {
        if(user.getBalance() < club.joinClubPrice){
            throw new LowBalanceException();
        }
        user.setBalance(user.getBalance() - club.joinClubPrice);
    }



    public void addBookingPlayer(User user, int id) {
        Booking booking = bd.getBooking(id);
        ((BlindBooking) booking).addPlayer(user);
    }


    
    void releaseSpot(User user, int id){
        Booking booking = bd.getBooking(id);
        ((BlindBooking)booking).removePlayer(user);
        if(booking.getPlayers().isEmpty())
            releaseField(id);
    }

    void createBooking(Sport sport, UserClub clb, Field field, LocalDate date, int hour, ArrayList<User> players){
        holdField(field, date, hour);
        for(User u : players)
            pay(u, clb);
        Booking booking = new PrivateBooking(clb.getClub(), field, date, hour, players);
        bd.addBooking(booking);
    }

    void createBlindBooking(Sport sport, UserClub clb, Field field, LocalDate date, int hour, User user){
        holdField(field, date, hour);
        pay(user, clb);
        ArrayList<User> players = new ArrayList<>();
        players.add(user);
        Booking booking = new BlindBooking(clb.getClub(), field, date, hour, players);
        bd.addBooking(booking);
    }
}
