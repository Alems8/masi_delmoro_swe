package DAO;

import BusinessLogic.BlindBooking;
import BusinessLogic.Booking;
import Sport.Sport;
import User.User;

import java.util.ArrayList;
import java.util.Set;

public interface BookingDao {
    Booking getBooking(int id);

    Set<Integer> getKeySet();

    void addBooking(Booking booking);

    void removeBooking(Booking booking);

    int getBookingsSize();

    void removeBookingPlayer(User user, int id);

    ArrayList<Integer> getUserKeys(User user);

    ArrayList<Integer> getBlindKeys(Sport sport);

}
