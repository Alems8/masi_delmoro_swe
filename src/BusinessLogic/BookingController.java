package BusinessLogic;

import DAO.BookingDao;
import DAO.FakeBookingDao;
import User.User;

import java.util.ArrayList;


public class BookingController {
    private final UserController uc;
    private final ClubController cc;
    private final BookingDao bookingDao;
    private Booking currentBooking;
    public BookingController(ClubController cc, UserController uc){
        this.uc = uc;
        this.cc = cc;
        this.bookingDao = FakeBookingDao.getInstance();
    }

    void setCurrentBooking(int id) throws WrongKeyException {
        Booking booking = bookingDao.getBooking(id);
        if(booking == null)
            throw new WrongKeyException();
        this.currentBooking = booking;
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

    void checkBlindBooking() throws WrongKeyException, NoFreeSpotException {
        if(currentBooking instanceof PrivateBooking)
            throw new WrongKeyException();
        if(((BlindBooking)currentBooking).isFull())
            throw new NoFreeSpotException();
    }

    public void addBookingPlayer() {
        try {checkBlindBooking();}
        catch (WrongKeyException e) {
            System.out.print("La chiave inserita non è corretta");
            return;
        }
        catch(NoFreeSpotException e) {
            System.out.println("Nessun posto libero in questa partita");
            return;
        }
        try{uc.payBooking(currentBooking.getClub(), currentBooking.getField());}
        catch(LowBalanceException e){
            System.out.println("Il tuo credito non è sufficiente");
            return;
        }
        ((BlindBooking)currentBooking).addPlayer(uc.getCurrentUser());
    }
}
