package BusinessLogic;

import DAO.BookingDao;
import DAO.FakeBookingDao;
import User.User;

import java.util.ArrayList;


public class BookingController {
    private final UserController uc;
    private final ClubController cc;
    private final BookingDao bookingDao;
    public BookingController(ClubController cc, UserController uc){
        this.uc = uc;
        this.cc = cc;
        this.bookingDao = FakeBookingDao.getInstance();
    }

    public void createBooking() {
        for (User u : uc.getCurrentPlayers()) {
            uc.setCurrentUser(u);
            try {
                uc.payBooking(cc.getCurrentClub(), cc.getCurrentField());
            } catch (LowBalanceException e) {
                System.out.println("Il saldo di "+u.username+" non è sufficiente");
                return;
            }
        }
        cc.holdField();

        Booking booking = new PrivateBooking(cc.getCurrentClub(), cc.getCurrentField(), cc.getCurrentDate(), cc.getCurrentHour(), uc.getCurrentPlayers());
        bookingDao.addBooking(booking);
    }

    public void createBlindBooking() {
        try {
            uc.payBooking(cc.getCurrentClub(), cc.getCurrentField());
        } catch (LowBalanceException e) {
            System.out.println("Il tuo saldo non è sufficiente");
            return;
        }
        cc.holdField();

        Booking booking = new BlindBooking(cc.getCurrentClub(), cc.getCurrentField(), cc.getCurrentDate(), cc.getCurrentHour(), uc.getCurrentUser());
        bookingDao.addBooking(booking);
    }
}
