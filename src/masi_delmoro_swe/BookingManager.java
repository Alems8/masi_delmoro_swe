/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

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
    private Map<Integer, Booking> blindBookings = new HashMap();
    
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
    
    //private boolean checkBookings(User user, LocalDate date, int hour){
    //    for(int i=)
    //}
    
    private User checkUser(String usernm){
        for(User u : users){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        return null;
    }
    
    public boolean requestBooking(String clb, String date, int hour, User user1, String username2, String username3, String username4) {
        LocalDate day = LocalDate.parse(date, dtf);
        Club club = checkClub(clb);
        if(club == null){
             System.out.println("sonoio1");
             return false;//fix me     
        }
        
        User user2 = checkUser(username2);
        User user3 = checkUser(username3);
        User user4 = checkUser(username4);
        ArrayList<User> players = new ArrayList<>(){{add(user1);add(user2);add(user3);add(user4);}};
        for(int i=1; i<=players.size();i++){
            if(players.get(i-1) == null){
                System.out.println("L'utente "+i + " non è registrato");
                return false;//fix me
            }
        }
        
        /*
        for(User u : players){
            if(!checkBookings(u, day, hour)){
                System.out.println(u.username + " è già impegnato");
                return false;
            }
        }
*/
        
        for(User u : players){
            if(!pay(u, club)){
                System.out.println(u.username + " non ha credito sufficiente");
                return false;//fix me
            }
        }
        
        Field field = checkField(day, hour, club); 
        
        if(field == null){
            for(User u : players)
                refund(u, club);
            System.out.println("sonoio4");
            return false;
            
        }
        Booking booking = new Booking(club, field, day, hour, players);
        bookings.put(bookings.size()+1, booking);
        return true;
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }
    
    public void cancelBooking(Integer key){
        Booking booking = bookings.remove(key);
        booking.getField().timeTable.get(booking.getDate()).add(booking.getHour());
        ArrayList <User> users = booking.getPlayers();
        for(User u : users){
            refund(u, booking.getClub());
        }
    }
    
    public void displayUserBookings(User user){
        for (int i = 1; i <= bookings.size(); i++){
            Booking booking = bookings.get(i);
            if(booking.getPlayers().indexOf(user) != -1){
                System.out.println(i+" "+booking.getClub().name+" "+booking.getField().name+" "+
                        booking.getDate().format(dtf)+" "+booking.getHour()+" "+
                        (booking.getIsBlind()?"Partita al buio":"Partita privata"));
            }
        }
    }
    
    public boolean requestBlindBooking(String clb, String date, int hour, User user){
        Club club = checkClub(clb);
        if(club == null){
             System.out.println("sonoio1");
             return false;//fix me    
        }
        
        //Aggiungere checkUser?
        
        if(! (pay(user, club)) ){
            return false;
        }
        
        LocalDate day = LocalDate.parse(date, dtf);
        Field field = checkField(day, hour, club);
        
        if(field == null){
            refund(user, club);
            return false;
        }
        
        ArrayList<User> players = new ArrayList<>(){{add(user);}};
        Booking booking = new Booking(club, field, day, hour, players);
        booking.setIsBlind(true);
        blindBookings.put(blindBookings.size()+1, booking);
        
        return true;
        
    }
    
    public void displayBlindBookings(){
        if(blindBookings.isEmpty()){
            System.out.println("Nessuna partita disponibile");
        }
        for(int i = 1; i <= blindBookings.size(); i++){
            Booking booking = blindBookings.get(i);
            System.out.println(i+" "+booking.getClub().name+" "+booking.getField().name+" "+
                    booking.getDate().format(dtf)+" "+booking.getHour()+" Posti prenotati: "+
                    booking.getPlayers().size());
        }
    }
    
    public boolean requestSpotBooking(int key, User user){ //FIX ME
        if(!blindBookings.containsKey(key))
            return false;
        
        Booking booking = blindBookings.get(key);
        
        if(!pay(user, booking.getClub()))
            return false;
        
        booking.getPlayers().add(user);
        if(booking.getPlayers().size() == 4)
            bookings.put(bookings.size()+1, blindBookings.remove(key));
        return true;
    }
    
    private Field checkField(LocalDate day, int hour, Club club){
        int i = 0;
        boolean booked = false;
        Field field = null;
        while( booked == false && i < club.fields.size() ){
            field = club.fields.get(i++);
            
            if( !(field.timeTable.containsKey(day)) ){
                ArrayList<Integer> updatedTimes = new ArrayList<>(club.times);
                int j = updatedTimes.indexOf(hour);
                updatedTimes.remove(j);
                field.timeTable.put(day, updatedTimes);
                booked = true;
            }
            
            else{
                ArrayList<Integer> times = field.timeTable.get(day);
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
}
