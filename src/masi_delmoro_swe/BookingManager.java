/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Alessio
 */
public class BookingManager {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    private Map<Integer, Booking> bookings = new HashMap();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int key = 1;
    
    public User addUser(Person person, String username) { //MODIFICATO
        User user = new User(username, person, this);
        users.add(user);
        return user;
    }
    
    public void addClub(Club club) {
        clubs.add(club);
    }
    
    private Club checkClub(String clb) {
        for(Club c : clubs) {
            if(c.name.equals(clb))
                    return c;
        }
        return null;
    }
    private boolean pay(User user, Club club){
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        
        if(user.getBalance() < price){
            return false;
        }
        user.setBalance(user.getBalance() - price);
        return true;
    }
    
    private void refund(User user, Club club){
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;
        user.setBalance(user.getBalance() + price);
    }
    
    private User checkUser(String usernm){
        for(User u : users){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        return null;
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }
    
    private Field checkField(Club club, String sport, LocalDate date, int hour){
        int i = 0;
        boolean booked = false;
        Field field = null;
        while( booked == false && i < club.fields.size() ){
            field = club.fields.get(i++);
            
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
        if(!booked)
            return null;
        return field;
    }

    public boolean addUserFavouriteClub(User user, String clb) {
        Club club = checkClub(clb);
        if(club == null)
            return false;
        user.favouriteClubs.add(club);
        return true;
    }
    /*
    public void addResult(String username1, String username2, int id){
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
*/
    
    public void displayUserRecord(User user){
        System.out.println("Il tuo storico è: " + user.record[1] + " vittorie - "
                            + user.record[0] + " sconfitte");
    }
    
    public boolean requestJoinClub(User user, String clb){
        Club club = checkClub(clb);
        if(clb == null){
            System.out.println("Il club non è iscritto al servizio");
            return false;
        }
        if(club.isMember(user)){
            System.out.println("Sei già iscritto al club");
            return false;
        }
        if(!payJoinClub(user, club))
            return false;
        club.addMember(user);
        return true;
            
    }
    
    public boolean payJoinClub(User user, Club club){
        if(user.getBalance() < club.joinClubPrice){
            System.out.println("Saldo insufficiente");
            return false;
        }
        user.setBalance(user.getBalance() - club.joinClubPrice);
        return true;
    }
    
    public boolean requestBooking(String sport, String clb, String day, int hour, User user){
        LocalDate date = LocalDate.parse(day, dtf);
        Club club = checkClub(clb);
        if(club == null)
            return false;
        Field field = checkField(club, sport, date, hour);
        if(field == null)
            return false;
        int size = field.sport.numPlayers;
        ArrayList<User> players = new ArrayList<>();
        players.add(user);
        System.out.println("Inserisci i nomi utente degli altri giocatori");
        for(int i=0; i<size-1; i++){
            Scanner scanner = new Scanner(System.in);
            User u = checkUser(scanner.next());
            if(u != null)
                players.add(u);
            else{
                System.out.println("Il giocatore non esiste");
                return false;
            }
        }
        for(User u : players){
            if(!pay(u, club))
                return false;
            //libera campo
        }
        Booking booking = new PrivateBooking(club, field, date, hour, players);
        bookings.put(key++, booking);
        return true;
    }
    
    public boolean requestBlindBooking(String sport, String clb, String day, int hour, User user){
        LocalDate date = LocalDate.parse(day, dtf);
        Club club = checkClub(clb);
        if(club == null)
            return false;
        Field field = checkField(club, sport, date, hour);
        if(field == null)
            return false;
        Booking booking = new BlindBooking(club, field, date, hour, user);
        bookings.put(key++, booking);
        return true;
    }
    
    public boolean requestSpot(int id, User user){
        Booking booking = bookings.get(id);
        if(booking == null){
            System.out.println("");
            return false;
        }
        ((BlindBooking) booking).addUser(user);
            return true;
    }
}
