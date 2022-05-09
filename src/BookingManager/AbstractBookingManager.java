package BookingManager;

import Sport.Sport;

import java.util.ArrayList;

public abstract class AbstractBookingManager {
    protected BookingDatabase bd;

    protected abstract void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users);
}
