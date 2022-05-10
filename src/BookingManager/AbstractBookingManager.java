package BookingManager;

import Person.Person;
import Sport.Sport;

import java.util.ArrayList;

public class AbstractBookingManager {
    protected BookingDatabase bd;

    public User addUser(Person person, String username) throws WrongNameException{
        if(bd.getUsersSize() == 0)
            return null;
        return bd.getUser(0);
    }

    void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users){}

    void deleteUserBooking(User user, int id){}

    void requestJoinClub(User user, String clb){}

    void topUpUserBalance(User user, int money){}

    void displayUserBookings(User user){}

    void requestBlindBooking(Sport sport, String clb, String day, int hour, User user){}

    void displayBlindBookings(){}

    void requestSpot(User user, int id){}

    void addFavouriteClub(User user, String clb){}

    void addMatchResult(ArrayList<String> winners, int id){}

    void displayUserRecord(User user){}

    void deleteUser(User user) throws PendingBookingException {}
}
