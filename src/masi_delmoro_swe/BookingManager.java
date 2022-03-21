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
    private ArrayList<Booking> blindBookings = new ArrayList<>();
    
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
    
    private User checkUsers(String usernm){
        for(User u : users){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        return null;
    }
    
    public boolean requestBooking(String clb, String date, int hour, User user1, String username2, String username3, String username4) {
        Club club = checkClub(clb);
        if(club == null){
             System.out.println("sonoio1");
             return false;//fix me     
        }
        
        User user2 = checkUsers(username2);
        User user3 = checkUsers(username3);
        User user4 = checkUsers(username4);
        if(user1 == null || user2 == null || user3 == null || user4 == null){
            System.out.println("sonoio2");
            return false;//fix me
        }
            
          
        
        if(! (pay(user1, club) && pay(user2, club) && pay(user3, club) && pay(user4, club) ) ){
            System.out.println("sonoio3");
            return false;//fix me
        }
             
        
        
        
        LocalDate day = LocalDate.parse(date, dtf);
        Field field = checkField(day, hour, club);
    /*    int i = 0;
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
        } */  
        
        if(field == null){
            refund(user1, club);
            refund(user2, club);
            refund(user3, club);
            refund(user4, club);
            System.out.println("sonoio4");
            return false;
            
        }
        ArrayList<User> players = new ArrayList<>(){{add(user1);add(user2);add(user3);add(user4);}};
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
    
    public void displayList(User user){
        for (int i = 1; i <= bookings.size(); i++){
            Booking booking = bookings.get(i);
            if(booking.getPlayers().indexOf(user) != -1){
                System.out.println(i+" "+booking.getClub().name+" "+booking.getField().name+" "+booking.getDate().format(dtf)+" "+booking.getHour());
            }
        }
    }
    
    public boolean requestBlindBooking(String clb, String date, int hour, User user){
        Club club = checkClub(clb);
        if(club == null){
             System.out.println("sonoio1");
             return false;//fix me    
        }
        
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
        blindBookings.add(booking);
        
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
