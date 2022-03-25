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
    
    private boolean checkBookings(User user, LocalDate date, int hour){
        if(!bookings.isEmpty()){
            //for(int i=1; i<=bookings.size(); i++){
            for(int k : bookings.keySet()){
                Booking booking = bookings.get(k);
                if(booking.getPlayers().contains(user)){
                    if(booking.getDate().equals(date)){
                        if(booking.getHour() == hour){
                            System.out.println("L'utente " + user.username + " è già impegnato");
                            return false;
                        }
                    }
                }
            }
        }
        if(!blindBookings.isEmpty()){
            //for(int i=1; i<=blindBookings.size(); i++){
            for(int k : blindBookings.keySet()){
                Booking booking = blindBookings.get(k);
                if(booking.getPlayers().contains(user)){
                    if(booking.getDate().equals(date)){
                        if(booking.getHour() == hour){
                            System.out.println("L'utente " + user.username + " è già impegnato");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
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
        
        for(User u : players){
            if(!checkBookings(u, day, hour))
                return false;
        }
        
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
        bookings.put(key++, booking);
        return true;
    }
    
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }
    
    public void cancelBooking(int id){
        Booking booking = bookings.remove(id);
        booking.getField().timeTable.get(booking.getDate()).add(booking.getHour());
        ArrayList <User> users = booking.getPlayers();
        for(User u : users){
            refund(u, booking.getClub());
        }
    }
    
    public void displayUserBookings(User user){
       // for (int i = 1; i <= bookings.size(); i++){
       for(int k : bookings.keySet()){
            Booking booking = bookings.get(k);
            if(booking.getPlayers().indexOf(user) != -1){
                System.out.println(k +  " "+booking.getClub().name+" "+booking.getField().name+" "+
                        booking.getDate().format(dtf)+" "+booking.getHour()+" "+
                        (booking.getIsBlind()?"Partita al buio ":"Partita privata ")+
                        booking.getPlayers().size()+" giocatori");
            }
        }
        for(int k : blindBookings.keySet()){
            Booking booking = blindBookings.get(k);
          if(booking.getPlayers().indexOf(user) != -1){
                System.out.println(k +  " "+booking.getClub().name+" "+booking.getField().name+" "+
                        booking.getDate().format(dtf)+" "+booking.getHour()+" "+
                        (booking.getIsBlind()?"Partita al buio ":"Partita privata ")+
                        booking.getPlayers().size()+" giocatori");
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
        
        if(!checkBookings(user, day, hour))
            return false;
        
        ArrayList<User> players = new ArrayList<>(){{add(user);}};
        Booking booking = new Booking(club, field, day, hour, players);
        booking.setIsBlind(true);
        blindBookings.put(key++, booking);
        
        return true;
        
    }
    
    public void displayBlindBookings(){
        if(blindBookings.isEmpty()){
            System.out.println("Nessuna partita disponibile");
        }
        //for(int i = 1; i <= blindBookings.size(); i++){
        for(int k : blindBookings.keySet()){
            Booking booking = blindBookings.get(k);
            System.out.println(k+" "+booking.getClub().name+" "+booking.getField().name+" "+
                    booking.getDate().format(dtf)+" "+booking.getHour()+" Posti prenotati: "+
                    booking.getPlayers().size());
        }
    }
    
    public boolean requestSpotBooking(int id, User user){ //FIX ME
        if(!blindBookings.containsKey(id))
            return false;
        
        Booking booking = blindBookings.get(id);
        
        if(!checkBookings(user, booking.getDate(), booking.getHour()))
            return false;
        
        if(!pay(user, booking.getClub()))
            return false;
        
        booking.getPlayers().add(user);
        if(booking.getPlayers().size() == 4)
            bookings.put(id, blindBookings.remove(id));
        return true;
    }
    
    public void cancelSpot(int id, User user){
        Booking booking = blindBookings.get(id);
        if(booking == null){
            booking = bookings.remove(id);
            blindBookings.put(id,booking);
        }
        booking.getPlayers().remove(user);
        refund(user, booking.getClub());
        if (booking.getPlayers().isEmpty()){
            blindBookings.remove(id);
            booking.getField().timeTable.get(booking.getDate()).add(booking.getHour());
        }
        
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

    public boolean addUserFavouriteClub(User user, String clb) {
        Club club = checkClub(clb);
        if(club == null)
            return false;
        user.favouriteClubs.add(club);
        return true;
    }
    
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
}
