package DAO;

import BusinessLogic.Booking;
import Club.Sport;
import User.User;

import java.util.ArrayList;
import java.util.Set;

public interface BookingDao {
    Booking getBooking(int id);

    Set<Integer> getKeySet();

    void addBooking(Booking booking);

    void removeBooking(Booking booking);

    int getBookingsSize();

    ArrayList<Integer> getUserKeys(User user);

    ArrayList<Integer> getBlindKeys(Sport sport);

}
