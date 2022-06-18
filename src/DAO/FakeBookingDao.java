package DAO;

import BusinessLogic.BlindBooking;
import BusinessLogic.Booking;
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

    public void removeBooking(int id){
        bookings.remove(id);
    }

    public int getBookingsSize(){
        return bookings.size();
    }

    public void removeBookingPlayer(User user, int id) {
        Booking booking = getBooking(id);
        ((BlindBooking)booking).removePlayer(user);
    }
}
