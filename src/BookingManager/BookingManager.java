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

import static org.junit.jupiter.api.Assertions.fail;


/**
 *
 * @author Alessio
 */
public class BookingManager {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    private Map<Integer, Booking> bookings = new HashMap();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BalanceMonitor monitor;
    private int key = 1;
    
    public BookingManager(BalanceMonitor monitor) {
        this.monitor = monitor;
    }

    public User addUser(Person person, String username) throws WrongNameException {
        try{checkUser(username);}
        catch (WrongNameException e) {
            User user = new User(username, person, monitor, this);    //TEST ME
            users.add(user);
            return user;
        }
        throw new WrongNameException();  //FIX ME
    }

    Map<Integer, Booking> getBookings() {
        return bookings;
    }

    ArrayList<User> getUsers() {
        return users;
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
    
    public void addClub(Club club) {
        clubs.add(club);
    }
    
    private Club checkClub(String clb) throws WrongNameException {
        for(Club c : clubs) {
            if(c.name.equals(clb))
                    return c;
        }
        throw new WrongNameException();
    }
    private void pay(User user, Club club) throws LowBalanceException {
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        
        if(user.getBalance() < price) throw new LowBalanceException();
        else
            user.setBalance(user.getBalance() - price);
    }
    
    private void refund(User user, Club club){
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        rechargeAccount(user, price);
    }
    
    private User checkUser(String usernm) throws WrongNameException {
        for(User u : users){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        throw new WrongNameException();
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }
    
    private Field checkField(Club club, Sport sport, LocalDate date, int hour) throws NoFreeFieldException {
        int i = 0;
        boolean booked = false;
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

    public void addUserFavouriteClub(User user, String clb) {
        Club club = null;
        try {club = checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }
        user.getFavouriteClubs().add(club);
    }
    
    public void addResult(String username1, String username2, int id){ //FIX ME
        Booking booking = bookings.remove(id);
        ArrayList<User> players = booking.getPlayers();
        for (User user : players){
            if(user.username.equals(username1))
                user.record[1]++;
            else if(user.username.equals(username2))
                user.record[1]++;
            else
                user.record[0]++;
        }
        
    }
    
    public void releaseField(int id){
        Booking booking = bookings.remove(id);
        booking.getField().timeTable.get(booking.getDate()).add(booking.getHour());
    }
    
    public void displayUserRecord(User user){
        System.out.println("Il tuo storico è: " + user.record[1] + " vittorie - "
                            + user.record[0] + " sconfitte");
    }
    
    public void requestJoinClub(User user, String clb){
        Club club = null;
        try{club = checkClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non è iscritto al servizio");
            return;
        }
        if(club.isMember(user)) throw new NullPointerException();{
            System.out.println("Sei già iscritto al club");
        }
        try{payJoinClub(user, club);}
        catch (LowBalanceException ex) {
            System.out.println("Non hai abbastanza credito per associarti al club");
            return;
        }
        club.addMember(user);

    }
    
    public void payJoinClub(User user, Club club) throws LowBalanceException {
        if(user.getBalance() < club.joinClubPrice){
            throw new LowBalanceException();
        }
        user.setBalance(user.getBalance() - club.joinClubPrice);
    }
    
    public void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users) {
        LocalDate date = LocalDate.parse(day, dtf);
        Club club = null;
        try{club = checkClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }

        Field field = null;
        try{field = checkField(club, sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }

        int size = field.sport.numPlayers;
        ArrayList<User> players = new ArrayList<>();
        for(int i=0; i<size; i++){
            User u = null;
            try {u = checkUser(users.get(i));}
            catch(WrongNameException e) {
                field.timeTable.get(date).add(hour);
                System.out.println("L'utente inserito non esiste");
                return;
            }

            try{pay(u, club);}
            catch(LowBalanceException e){
                field.timeTable.get(date).add(hour);
                System.out.println("L'utente non ha credito sufficiente");
                return;
            }
            players.add(u);
        }
        Booking booking = new PrivateBooking(club, field, date, hour, players);
        bookings.put(key++, booking);
    }

    private ArrayList<Integer> getUserKeys(User user) throws NoActiveBookingsException {
        ArrayList<Integer> userKeys = new ArrayList<>();
        for(int k : bookings.keySet()){
            Booking booking = bookings.get(k);
            if(booking.containsUser(user)){
                userKeys.add(k);
            }
        }
        if(userKeys.isEmpty())
            throw new NoActiveBookingsException();
        return userKeys;
    }

    public void displayUserBookings(User user) {
        ArrayList<Integer> keys = new ArrayList<>();
        try{keys = getUserKeys(user);}
        catch(NoActiveBookingsException e) {
            System.out.println("Non hai nessuna prenotazione");
        }
        for(int k : keys){
            System.out.println(k+bookings.get(k).toString());
        }
    }

    public void requestBlindBooking(Sport sport, String clb, String day, int hour, User user) {
        LocalDate date = LocalDate.parse(day, dtf);
        Club club = null;
        try {club = checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club inserito non è iscritto al servizio");
            return;
        }
        Field field = null;
        try{field = checkField(club, sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }
        ArrayList<User> players = new ArrayList<>();
        players.add(user);
        Booking booking = new BlindBooking(club, field, date, hour, players);
        bookings.put(key++, booking);
    }
    
    public void displayBlindBookings(){
        for(int k : bookings.keySet()){
            Booking booking = bookings.get(k);
            if(booking.getPlayers().size() < booking.getField().sport.numPlayers)
                System.out.println(k+booking.toString());
        }
    }

    private void checkBlindBooking(int id) throws WrongKeyException {
        Booking booking = bookings.get(id);
        if(booking == null || booking instanceof PrivateBooking) {
            throw new WrongKeyException();
        }
    }

    public void requestSpot(User user, int id){
        try{checkBlindBooking(id);}
        catch(WrongKeyException e) {
            System.out.print("Non puoi prenotare un posto in questa partita");
            return;
        }
        Booking booking = bookings.get(id);
        ((BlindBooking) booking).addPlayer(user);
    }
    
    public Booking checkBooking(User user, int id) throws WrongKeyException{
        Booking booking = null;
        for(int k : bookings.keySet()){
            if(k == id){
                booking = bookings.get(k);
                if(!booking.containsUser(user)){
                    throw new WrongKeyException();
                }
            }
        }
        return booking;
    }
    
    public void deleteUserBooking(User user, int id) {
        Booking booking = null;
        try {booking = checkBooking(user, id);}
        catch(WrongKeyException e) {
            System.out.println("Non hai diritti su questa prenotazione");
            return;
        }

        if(booking instanceof PrivateBooking) {
            releaseField(id);
        }
        else
            releaseSpot(user, id);
    }
    
    private void releaseSpot(User user, int id){
        Booking booking = bookings.get(id);
        ((BlindBooking)booking).removePlayer(user);
        if(booking.getPlayers().isEmpty())
            releaseField(id);
    }
}
