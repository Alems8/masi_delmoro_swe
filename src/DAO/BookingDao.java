package DAO;

import BusinessLogic.BlindBooking;
import BusinessLogic.Booking;
import User.User;

import java.util.Set;

public interface BookingDao {
    public Booking getBooking(int id);

    public Set<Integer> getKeySet();

    public void addBooking(Booking booking);

    public void removeBooking(int id);

    public int getBookingsSize();

    public void removeBookingPlayer(User user, int id);
}
