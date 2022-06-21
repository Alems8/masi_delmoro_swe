package BusinessLogic;

import Club.UserClub;
import DAO.BookingDao;
import DAO.FakeBookingDao;
import Club.Sport;
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

    Booking getCurrentBooking() {
        return currentBooking;
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

        Booking booking = new PrivateBooking(cc.getCurrentClub(), cc.getCurrentField(), cc.getCurrentDate(), cc.getCurrentHour(), new ArrayList<>(uc.getCurrentPlayers()));
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

    void removeBookingPlayer(){
        ((BlindBooking)currentBooking).removePlayer(uc.getCurrentUser());
    }

    void deleteBooking(){
        bookingDao.removeBooking(currentBooking);
    }

    void releaseField(){
        currentBooking.getField().timeTable.get(currentBooking.getDate()).add(currentBooking.getHour());
    }
    void deleteUserBooking() throws WrongKeyException {
        if(!currentBooking.getPlayers().contains(uc.getCurrentUser()))
            throw new WrongKeyException();
        UserClub club = currentBooking.getClub();
        int price = currentBooking.getField().price;
        if(club.isMember(uc.getCurrentUser()))
            price = price - price*(club.getClub().memberDiscount)/100;
        if(currentBooking instanceof BlindBooking){
            removeBookingPlayer();
            uc.refund(price);
            if (!currentBooking.getPlayers().isEmpty())
                return;
        }
        else{
            for(User u : currentBooking.getPlayers()) {
                uc.setCurrentUser(u);
                price = currentBooking.getField().price;
                if(club.isMember(uc.getCurrentUser()))
                    price = price - price*(club.getClub().memberDiscount)/100;
                uc.refund(price);
            }
        }
        deleteBooking();
        releaseField();
    }

    public void displayUserBookings() throws NoActiveBookingsException {
        ArrayList<Integer> keys = bookingDao.getUserKeys(uc.getCurrentUser());
        if(keys.isEmpty())
            throw new NoActiveBookingsException();
        for(int k : keys)
            System.out.println(k +bookingDao.getBooking(k).toString());
    }

    public void displayBlindBookings(Sport sport) throws NoActiveBookingsException {
        ArrayList<Integer> keys = bookingDao.getBlindKeys(sport);
        if(keys.isEmpty())
            throw new NoActiveBookingsException();
        for(int k : keys)
            System.out.println(k +bookingDao.getBooking(k).toString());
    }

    public void addMatchResult() {
        Sport sport = currentBooking.getField().sport;
        for (User u : currentBooking.getPlayers()){
            if(!(u.record.containsKey(sport))) {
                int[] result = new int[2];
                u.record.put(sport, result);
            }
            u.record.get(sport)[0]++;
            for(User w : uc.getCurrentPlayers()){
                if(w.equals(u)){
                    u.record.get(sport)[1]++;
                    u.record.get(sport)[0]--;
                }
            }
        }
        deleteBooking();
    }

    void displayUserRecord() {
        User user = uc.getCurrentUser();
        System.out.println("Il tuo storico è: ");
        for (Sport k : user.record.keySet()) {
            System.out.println(k.name + ": " + user.record.get(k)[1] + " vittorie - " + user.record.get(k)[0] +
                    " sconfitte");
        }
    }

    void checkActiveBookings() throws PendingBookingException {
        if(bookingDao.getUserKeys(uc.getCurrentUser()).size() != 0)
            throw new PendingBookingException();
    }
}
