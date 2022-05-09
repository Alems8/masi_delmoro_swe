package BookingManager;

import Booking.Booking;
import Booking.PrivateBooking;
import Booking.BlindBooking;
import Club.Club;
import Club.Field;
import Sport.Sport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookingProxy extends AbstractBookingManager{

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BookingManager bm;

    public BookingProxy(BookingManager bm){
        this.bm = bm;
    }

    public User checkUser(String usernm) throws WrongNameException {
        for(User u : bd.users){
            if(u.username.equals(usernm)){
                return u;
            }
        }
        throw new WrongNameException();
    }

    public void checkBalance(User user, UserClub club) throws LowBalanceException {
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;

        if(user.getBalance() < price)
            throw new LowBalanceException();
    }

    private UserClub checkClub(String clb) throws WrongNameException {
        for(UserClub c : bd.clubs) {
            if(c.getClub().name.equals(clb))
                return c;
        }
        throw new WrongNameException();
    }

    private Field checkField(UserClub clb, Sport sport, LocalDate date, int hour) throws NoFreeFieldException {
        int i = 0;
        boolean booked = false;
        Club club = clb.getClub();
        Field field = null;
        while(!booked && i < club.fields.size()){
            field = club.fields.get(i++);

            //Controllare se la richiesta è fuori orario
            if(field.sport.equals(sport)){
                if(!(field.timeTable.containsKey(date))){
                    ArrayList<Integer> updatedTimes = new ArrayList<>(club.times);
                    field.timeTable.put(date, updatedTimes);
                    booked = true;
                }
                else {
                    ArrayList<Integer> times = field.timeTable.get(date);
                    if(times.contains(hour)){
                        booked = true;
                    }
                }
            }
        }
        if(!booked)
            throw new NoFreeFieldException();
        return field;
    }

    @Override
    void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users) {
        LocalDate date = LocalDate.parse(day, dtf);
        UserClub club = null;
        try{club = checkClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }

        Field field = null;
        try{field = checkField(club, sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }

        int size = sport.numPlayers;
        ArrayList<User> players = new ArrayList<>();
        for(int i=0; i<size; i++){
            User u = null;
            try {u = checkUser(users.get(i));}
            catch(WrongNameException e) {
                System.out.println("L'utente inserito non esiste");
                return;
            }

            try{checkBalance(u, club);}
            catch(LowBalanceException e){
                System.out.println("L'utente non ha credito sufficiente");
                return;
            }
            players.add(u);
        }
        bm.createBooking(sport, club, field, date, hour, players);
    }

    private Booking checkBooking(User user, int id) throws WrongKeyException{
        Booking booking = null;
        for(int k : bd.bookings.keySet()){
            if(k == id){
                booking = bd.bookings.get(k);
                if(!booking.containsUser(user)){
                    throw new WrongKeyException();
                }
            }
        }
        return booking;
    }

    void deleteUserBooking(User user, int id) {
        Booking booking = null;
        try {booking = checkBooking(user, id);}
        catch(WrongKeyException e) {
            System.out.println("Non hai diritti su questa prenotazione");
            return;
        }

        if(booking instanceof PrivateBooking) {
            bm.releaseField(id);
        }
        else
            bm.releaseSpot(user, id);
    }

    private void checkJoinClubBalance(User user, UserClub club) throws LowBalanceException {
        if(user.getBalance() < club.joinClubPrice)
            throw new LowBalanceException();
    }

    void requestJoinClub(User user, String clb){
        UserClub club = null;
        try{club = checkClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non è iscritto al servizio");
            return;
        }
        if(!club.isMember(user)) {
            try{checkJoinClubBalance(user, club);}
            catch (LowBalanceException ex) {
                System.out.println("Non hai abbastanza credito per associarti al club");
                return;
            }
            bm.addClubMember(user, club);
        }
    }

    void topUpBalance(User user, int money){
        bm.topUpUserBalance(user, money);
    }

    private ArrayList<Integer> getUserKeys(User user) throws NoActiveBookingsException {
        ArrayList<Integer> userKeys = new ArrayList<>();
        for(int k : bd.bookings.keySet()){
            Booking booking = bd.bookings.get(k);
            if(booking.containsUser(user)){
                userKeys.add(k);
            }
        }
        if(userKeys.isEmpty())
            throw new NoActiveBookingsException();
        return userKeys;
    }

    void displayUserBookings(User user){
        ArrayList<Integer> keys = new ArrayList<>();
        try{keys = getUserKeys(user);}
        catch(NoActiveBookingsException e) {
            System.out.println("Non hai nessuna prenotazione");
        }
        bm.displayBookings(keys);
    }

    @Override
    void requestBlindBooking(Sport sport, String clb, String day, int hour, User user) {
        LocalDate date = LocalDate.parse(day, dtf);
        UserClub club = null;
        try {club = checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club inserito non è iscritto al servizio");
            return;
        }
        Field field = null;
        try{field = checkField(club, sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }
        try{checkBalance(user, club);}
        catch(LowBalanceException e){
            System.out.println("Non hai abbastanza credito");
        }
        bm.createBlindBooking(sport, club, field, date, hour, user);
    }
}
