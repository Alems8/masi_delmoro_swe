/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;


import Club.Club;
import Club.UserClub;
import Club.Field;
import User.Person;
import User.User;
import Sport.Sport;
import ObserverUtil.Observer;

import java.util.ArrayList;
import java.time.LocalDate;



/**
 *
 * @author Alessio
 */
public class BookingManager {
    BookingDatabase bd;
    private static BookingManager instance = null;

    private BookingManager() {
        this.bd = new BookingDatabase();
    }
    public static BookingManager getInstance() {
        if(instance == null)
            instance = new BookingManager();
        return instance;
    }

//    public User createUser(Person person, String username, BookingChecker bc){
//        User user = new User(username, person, bc);
//        Observer monitor = BalanceMonitor.getInstance();
//        user.addObserver(monitor);
//        bd.addUser(user);
//        return user;
//    }

    public void removeUser(User user){
        bd.removeUser(user);
        System.out.println("Utente rimosso correttamente");
    }

    public int getUsersSize() {
        return bd.getUsersSize();
    }

    public User getUser(int id){
        return bd.getUser(id);
    }

    void pay(User user, UserClub club, Field field) {
        int price = field.price;
        if(club.isMember(user))
            price = price - price*club.getClub().memberDiscount/100;
        user.setBalance(user.getBalance() - price);
    }

    void holdField(Field field, LocalDate date, int hour){
        ArrayList<Integer> times = field.timeTable.get(date);
        times.remove((Integer) hour);
    }

    void topUpUserBalance(User user, int money){
        user.setBalance(user.getBalance() + money);
    }

    void refund(User user, UserClub club, Field field){
        int price = field.price;
        if(club.isMember(user))
            price = price - price*club.getClub().memberDiscount/100;
        rechargeAccount(user, price);
    }

    public Booking getBooking(int id) {
        return bd.getBooking(id);
    }

    void displayBookings(ArrayList<Integer> keys){
        for(int k : keys)
            System.out.println(k+bd.getBooking(k).toString());
    }

    public UserClub addClub(Club clb, int joinClubPrice) {
        UserClub club = new UserClub(clb, joinClubPrice);
        Observer monitor = MembersMonitor.getInstance();
        clb.addObserver(monitor);
        bd.addClub(club);
        return club;
    }

    public int getClubsSize(){
        return bd.getClubsSize();
    }

    public UserClub getClub(int id){
        return bd.getClub(id);
    }

    void addClubMember(User user, UserClub club){
        payJoinClub(user, club);
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
            if(!(u.record.containsKey(sport))) {
                int[] result = new int[2];
                u.record.put(sport, result);
            }
            u.record.get(sport)[0]++;
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

    
    public void payJoinClub(User user, UserClub club) {
        user.setBalance(user.getBalance() - club.joinClubPrice);
    }


    public void addBookingPlayer(User user, int id) {
        Booking booking = bd.getBooking(id);
        pay(user,booking.getClub(), booking.getField());
        ((BlindBooking) booking).addPlayer(user);
    }


    void releaseSpot(User user, int id){
        Booking booking = bd.getBooking(id);
        refund(user, booking.getClub(), booking.getField());
        bd.removeBookingPlayer(user,id);
        if(booking.getPlayers().isEmpty())
            releaseField(id);
    }

    void createBooking(UserClub clb, Field field, LocalDate date, int hour, ArrayList<User> players){
        holdField(field, date, hour);
        for(User u : players)
            pay(u, clb, field);
        Booking booking = new PrivateBooking(clb, field, date, hour, players);
        bd.addBooking(booking);
    }

    void createBlindBooking(UserClub clb, Field field, LocalDate date, int hour, User user){
        holdField(field, date, hour);
        pay(user, clb, field);
        ArrayList<User> players = new ArrayList<>();
        players.add(user);
        Booking booking = new BlindBooking(clb, field, date, hour, user);
        bd.addBooking(booking);
    }
}
