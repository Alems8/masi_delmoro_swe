package BookingManager;

import Sport.Sport;

import java.util.ArrayList;

public class AbstractBookingManager {
    protected BookingDatabase bd;

    void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users){
    }

    void deleteUserBooking(User user, int id){}

    void requestJoinClub(User user, String clb){}

}
