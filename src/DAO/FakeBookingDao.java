package DAO;

import BusinessLogic.BlindBooking;
import BusinessLogic.Booking;
import Sport.Sport;
import User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FakeBookingDao implements BookingDao{
    private final Map<Integer, Booking> bookings;
    private int key = 1;

    private static FakeBookingDao instance = null;

    private FakeBookingDao() {
        bookings = new HashMap<>();
    }

    public static FakeBookingDao getInstance(){
        if (instance == null){
            instance = new FakeBookingDao();
        }
        return instance;
    }


    public Booking getBooking(int id){
        return bookings.get(id);
    }

    public Set<Integer> getKeySet(){
        return bookings.keySet();
    }

    public void addBooking(Booking booking){
        bookings.put(key++, booking);
    }

    public void removeBooking(Booking booking){
        bookings.remove(booking);
    }

    public int getBookingsSize(){
        return bookings.size();
    }

    public ArrayList<Integer> getUserKeys(User user){
        ArrayList<Integer> userKeys = new ArrayList<>();
        for(int i=1; i<bookings.size(); i++){
            if(bookings.get(i).getPlayers().contains(user))
                userKeys.add(i);
        }
        return userKeys;
    }

    public void removeBookingPlayer(User user, int id) {
        Booking booking = getBooking(id);
        ((BlindBooking)booking).removePlayer(user);
    }

    public ArrayList<Integer> getBlindKeys(Sport sport){
        ArrayList<Integer> blindKeys = new ArrayList<>();
        for(int i=1; i<bookings.size(); i++){
            Booking b = bookings.get(i);
            if(b instanceof BlindBooking && !((BlindBooking) b).isFull() && b.getField().sport.equals(sport))
                blindKeys.add(i);
        }
        return blindKeys;
    }
}
