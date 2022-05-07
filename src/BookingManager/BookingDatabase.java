package BookingManager;

import Booking.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingDatabase {

    private final ArrayList<User> users = new ArrayList<>(); //TODO FIXME
    private final ArrayList<UserClub> clubs = new ArrayList<>(); //TODO FIXME
    private final Map<Integer, Booking> bookings = new HashMap(); //TODO FIXME


    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<UserClub> getClubs() {
        return clubs;
    }

    public Map<Integer, Booking> getBookings() {
        return bookings;
    }

}
