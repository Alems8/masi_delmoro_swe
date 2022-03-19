/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author Alessio
 */
public class BookingManager {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    
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
    
    public boolean requestBooking(String clb, String date, int hour, String username1, String username2, String username3, String username4) {
        Club club = checkClub(clb);
        if(club == null)
            return false; //fix me
        
        User user1 = checkUsers(username1);
        User user2 = checkUsers(username2);
        User user3 = checkUsers(username3);
        User user4 = checkUsers(username4);
        if(user1 == null || user2 == null || user3 == null || user4 == null)
            return false; //fix me
        
        if(! (pay(user1, club) && pay(user2, club) && pay(user3, club) && pay(user4, club) ) )
            return false; //fix me
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate day = LocalDate.parse(date, dtf);
        
        int i = 0;
        boolean booked = false;
        while( booked == false && i < club.fields.size() ){
            Field field = club.fields.get(i++);
            
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
        
        if(!booked){
            refund(user1, club);
            refund(user2, club);
            refund(user3, club);
            refund(user4, club);
            return false;
        }
        return true;
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }
}
