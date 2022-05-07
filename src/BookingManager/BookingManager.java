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

import java.util.*;
import java.time.LocalDate;



/**
 *
 * @author Alessio
 */
public class BookingManager {

    private final BalanceMonitor monitor; //TODO FIXME
    private final UserInterface ui; //TODO FIXME
    final BookingDatabase bd; //TODO FIXME
    private int key = 1;


    public BookingManager(BalanceMonitor monitor) {
        this.ui = new UserInterface(this);
        this.bd = new BookingDatabase();
        this.monitor = monitor;
    }


    public UserClub addClub(Club clb, int memberPrice, int joinClubPrice) {
        UserClub club = new UserClub(clb, this, memberPrice, joinClubPrice);
        bd.getClubs().add(club);
        return club;
    }
    public UserClub checkClub(String clb) throws WrongNameException {
        for(UserClub c : bd.getClubs()) {
            if(c.getClub().name.equals(clb))
                return c;
        }
        throw new WrongNameException();
    }
    public void payJoinClub(User user, UserClub club) throws LowBalanceException {
        if(user.getBalance() < club.joinClubPrice){
            throw new LowBalanceException();
        }
        user.setBalance(user.getBalance() - club.joinClubPrice);
    }



    public User addUser(Person person, String username) throws WrongNameException {
        try{checkUser(username);}
        catch (WrongNameException e) {
            User user = new User(username, person, ui, monitor);
            bd.getUsers().add(user);
            return user;
        }
        throw new WrongNameException();
    }
    User checkUser(String usernm) throws WrongNameException {
        for(User u : bd.getUsers()){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        throw new WrongNameException();
    }
    void removeUser(User user){
        bd.getUsers().remove(user);
    }
    ArrayList<Integer> getUserKeys(User user) throws NoActiveBookingsException {
        ArrayList<Integer> userKeys = new ArrayList<>();
        for(int k : bd.getBookings().keySet()){
            Booking booking = bd.getBookings().get(k);
            if(booking.containsUser(user)){
                userKeys.add(k);
            }
        }
        if(userKeys.isEmpty())
            throw new NoActiveBookingsException();
        return userKeys;
    }



    void pay(User user, UserClub club) throws LowBalanceException {
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;

        if(user.getBalance() < price) throw new LowBalanceException();
        else
            user.setBalance(user.getBalance() - price);
    }
    private void refund(User user, UserClub club){
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        //rechargeAccount(user, price); //TODO FIX ME
    }



    void createBooking(Booking booking){
        bd.getBookings().put(key++, booking);

    }
    Booking deleteBooking(int id){
        return bd.getBookings().remove(id);
    }
    Booking checkBooking(User user, int id) throws WrongKeyException{
        Booking booking = null;
        for(int k : bd.getBookings().keySet()){
            if(k == id){
                booking = bd.getBookings().get(k);
                if(!booking.containsUser(user)){
                    throw new WrongKeyException();
                }
            }
        }
        return booking;
    }
    Booking searchBooking(int id){
        return bd.getBookings().get(id);
    }
    Set<Integer> getKeys(){
        return bd.getBookings().keySet();
    }

    void checkBlindBooking(int id) throws WrongKeyException {
        Booking booking = bd.getBookings().get(id);
        if(booking == null || booking instanceof PrivateBooking) {
            throw new WrongKeyException();
        }
    }
    void releaseSpot(User user, int id){
        Booking booking = bd.getBookings().get(id);
        ((BlindBooking)booking).removePlayer(user);
        if(booking.getPlayers().isEmpty())
            releaseField(id);
    }

    

    Field checkField(UserClub clb, Sport sport, LocalDate date, int hour) throws NoFreeFieldException {
        int i = 0;
        boolean booked = false;
        Club club = clb.getClub();
        Field field = null;
        while( !booked && i < club.fields.size() ){
            field = club.fields.get(i++);
            
            //Controllare se la richiesta è fuori orario
            if(field.sport.equals(sport)){
                if( !(field.timeTable.containsKey(date)) ){
                    ArrayList<Integer> updatedTimes = new ArrayList<>(club.times);
                    int j = updatedTimes.indexOf(hour);
                    updatedTimes.remove(j);
                    field.timeTable.put(date, updatedTimes);
                    booked = true;
                }

                else{
                    ArrayList<Integer> times = field.timeTable.get(date);
                    int k = times.indexOf(hour);
                    if(k != -1){
                        times.remove(k);
                        booked = true;
                    }
                }
            }
        }
        if(!booked)
            throw new NoFreeFieldException();
        return field;
    }
    void releaseField(int id){
        Booking booking = bd.getBookings().remove(id);
        booking.getField().timeTable.get(booking.getDate()).add(booking.getHour());
    }






    

    





    


}
