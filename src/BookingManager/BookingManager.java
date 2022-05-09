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
    private BookingDatabase bd;
    
    public BookingManager(BalanceMonitor monitor) {
        this.monitor = monitor;
        this.bd = new BookingDatabase();
    }

    public User addUser(Person person, String username) throws WrongNameException {
        try{checkUser(username);}
        catch (WrongNameException e) {
            User user = new User(username, person, this, monitor);    //TEST ME
            users.add(user);
            return user;
        }
        throw new WrongNameException();  //FIX ME
    }

    private User getUser(String usernm) {
        for(User u : bd.users){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        return null;
    }

    public void deleteUser(User user) throws PendingBookingException {
        try{getUserKeys(user);}
        catch(NoActiveBookingsException e) {
            users.remove(user);
            System.out.println("Utente rimosso correttamente");
            return;
        }
        throw new PendingBookingException(); //TEST ME
    }

    private void pay(User user, UserClub club) {
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        user.setBalance(user.getBalance() - price);
    }

    private void holdField(Field field, LocalDate date, int hour){
        field.timeTable.get(date).remove(hour);
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

    Map<Integer, Booking> getBookings() {
        return bookings;
    }

    void displayBookings(ArrayList<Integer> keys){
        for(int k : keys)
            System.out.println(k+bd.bookings.get(k).toString());
    }

    public UserClub addClub(Club clb, int memberPrice, int joinClubPrice) {
        UserClub club = new UserClub(clb, this, memberPrice, joinClubPrice);
        clubs.add(club);
        return club;
    }

    void addClubMember(User user, UserClub club){
        club.addMember(user);
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }

    public void addUserFavouriteClub(User user, String clb) {
        UserClub club = null;
        try {club = checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }
        user.getFavouriteClubs().add(club);
    }
    
    public void addResult(ArrayList<String> winners, int id) throws WrongNameException {
        Booking booking = bookings.remove(id);
        ArrayList<User> players = booking.getPlayers();
        Sport sport = booking.getField().sport;
        if(sport.numPlayers / 2 != winners.size())
            throw new WrongNameException();
        for(String w : winners) {
            if(!players.contains(checkUser(w)))
                throw new WrongNameException();
        }
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
        Booking booking = bd.bookings.remove(id);
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



    public void displayUserBookings(User user) {

    }

    public void requestBlindBooking(Sport sport, String clb, String day, int hour, User user) {

    }
    
    public void displayBlindBookings(){
        for(int k : bookings.keySet()){
            Booking booking = bookings.get(k);
            if(booking instanceof BlindBooking && !((BlindBooking) booking).isFull())
                System.out.println(k+booking.toString());
        }
    }

    private void checkBlindBooking(int id) throws WrongKeyException {
        Booking booking = bookings.get(id);
        if(booking == null || booking instanceof PrivateBooking) {
            throw new WrongKeyException();
        }
    }

    public void requestSpot(User user, int id) {
        try {
            checkBlindBooking(id);
        } catch (WrongKeyException e) {
            System.out.print("Non puoi prenotare un posto in questa partita");
            return;
        }
        Booking booking = bookings.get(id);
        ((BlindBooking) booking).addPlayer(user);
    }
    

    
    void releaseSpot(User user, int id){
        Booking booking = bd.bookings.get(id);
        ((BlindBooking)booking).removePlayer(user);
        if(booking.getPlayers().isEmpty())
            releaseField(id);
    }

    void createBooking(Sport sport, UserClub clb, Field field, LocalDate date, int hour, ArrayList<User> players){
        holdField(field, date, hour);
        for(User u : players)
            pay(u, clb);
        Booking booking = new Booking(clb.getClub(), field, date, hour, players);
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
