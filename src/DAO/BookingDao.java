package DAO;

import BusinessLogic.BlindBooking;
import BusinessLogic.Booking;
import User.User;

import java.util.ArrayList;
import java.util.Set;

public interface BookingDao {
    public Booking getBooking(int id);

    public Set<Integer> getKeySet();

    public void addBooking(Booking booking);

    public void removeBooking(Booking booking);

    public int getBookingsSize();

    public void removeBookingPlayer(User user, int id);

    public ArrayList<Integer> getUserKeys(User user);

}
