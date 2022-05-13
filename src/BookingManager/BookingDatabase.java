package BookingManager;

import Booking.Booking;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class BookingDatabase {
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<UserClub> clubs = new ArrayList<>();
    private final Map<Integer, Booking> bookings = new HashMap<>();
    private int key = 1;

    User getUser(int id){
        return users.get(id);
    }

    void addUser(User user) {
        users.add(user);
    }

    void removeUser(User user){
        users.remove(user);
    }

    int getUsersSize(){
        return users.size();
    }

    boolean containsUser(User user){
        return users.contains(user);
    }


    void addClub(UserClub club){
        clubs.add(club);
    }

    UserClub getClub(int id){
        return clubs.get(id);
    }

    int getClubsSize(){
        return clubs.size();
    }


    Booking getBooking(int id){
        return bookings.get(id);
    }

    Set<Integer> getKeySet(){
        return bookings.keySet();
    }

    void addBooking(Booking booking){
        bookings.put(key++, booking);
    }

    void removeBooking(int id){
        bookings.remove(id);
    }

    int getBookingsSize(){
        return bookings.size();
    }
}
