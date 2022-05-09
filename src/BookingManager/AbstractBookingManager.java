package BookingManager;

public abstract class AbstractBookingManager {
    protected BookingDatabase bd;

    protected abstract void requestBooking();
}
