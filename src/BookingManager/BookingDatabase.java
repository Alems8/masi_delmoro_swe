package BookingManager;

import Booking.Booking;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BookingDatabase {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<UserClub> clubs = new ArrayList<>();
    Map<Integer, Booking> bookings = new HashMap<>();
    private int key = 1;

    void addBooking(Booking booking){
        this.bookings.put(key++, booking);
    }
}
