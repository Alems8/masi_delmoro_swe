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
            if(c.name == clb)
                    return c;
        }
        return null;
    }
    private boolean pay(){
        return true;//FIXME
    }
    
    private boolean checkUsers(){
        return true; //FIXME
    }
    
    public boolean requestBooking(String clb, String date, int hour, String user1, String user2, String user3, String user4) {
        Club club = checkClub(clb);
        if(club == null)
            return false; //fix me
        if(!checkUsers())
            return false; //fix me
        
        if(!pay())
            return false; //fix me
        
        Date day = new Date(date); //MODIFICATO
        for(Field field : club.fields) {
            if(field.timeTable.containsKey(day)){
                ArrayList<Integer> times = field.timeTable.get(day);
                if(times.contains(hour)){
                    times.remove(hour);
                    break;
                }
                    
                
                    
            }else{
              ArrayList<Integer> updatedTimes = club.times;
              updatedTimes.remove(hour);
              field.timeTable.put(day, updatedTimes);
              break;
            }
            
        }
        
        return true;
        
        
        
    }
}
