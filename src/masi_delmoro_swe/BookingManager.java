/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Alessio
 */
public class BookingManager {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    
    public void addUser(Person person, String username) {
        User user = new User(username, person, this);
        users.add(user);
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
    private boolean pay(User user){
        if(user.balance < 15){
            return false;
        }
        user.balance = user.balance - 15;
        return true;
    }
    
    private void refund(User user){
        user.balance = user.balance + 15;
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
        
        if(! (pay(user1) && pay(user2) && pay(user3) && pay(user4) ) )
            return false; //fix me
        
        Date day = new Date(date); //MODIFICATO
        
        int i = 0;
        boolean booked = false;
        while(booked == false || i < club.fields.size()){
            Field field = club.fields.get(i++);
            if(field.timeTable.containsKey(day)){
                ArrayList<Integer> times = field.timeTable.get(day);
                if(times.contains(hour)){
                    times.remove(hour);
                    booked = true;
                    
                }
                    
                
                    
            }else{
              ArrayList<Integer> updatedTimes = club.times;
              updatedTimes.remove(hour);
              field.timeTable.put(day, updatedTimes);
              booked = true;
              
            }
        }   
        
        if(!booked){
            refund(user1);
            refund(user2);
            refund(user3);
            refund(user4);
            return false;
        }
        return true;
        
        
        
    }
    
    public void rechargeAccount(User user, int money){
        user.balance = user.balance + money;
    }
}
