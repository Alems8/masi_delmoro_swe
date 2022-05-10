package BookingManager;

import Booking.Booking;
import Booking.PrivateBooking;
import Booking.BlindBooking;
import Club.Club;
import Club.Field;
import Person.Person;
import Sport.Sport;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookingChecker extends AbstractBookingManager{

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BookingManager bm;

    public BookingChecker(BookingManager bm, BookingDatabase bd){
        this.bm = bm;
        this.bd = bd;
    }

    public User addUser(Person person, String username) throws WrongNameException {
        try{checkUser(username);}
        catch (WrongNameException e) {
            return bm.createUser(person, username, this);
        }
        throw new WrongNameException();
    }

    public UserClub addClub(Club club, int memberPrice, int joinClubPrice){
        return bm.addClub(club, memberPrice, joinClubPrice);
    }

    private User checkUser(String usernm) throws WrongNameException {
        for(int i=0; i<bd.getUsersSize(); i++){
            User u = bd.getUser(i);
            if(u.username.equals(usernm)){
                return u;
            }
        }
        throw new WrongNameException();
    }

    private void checkBalance(User user, UserClub club) throws LowBalanceException {
        int price = club.price;
        if(club.isMember(user))
            price = club.memberPrice;

        if(user.getBalance() < price)
            throw new LowBalanceException();
    }

    private UserClub checkClub(String clb) throws WrongNameException {
        for(int i=0; i<bd.getClubsSize(); i++) {
            UserClub c = bd.getClub(i);
            if(c.getClub().name.equals(clb))
                return c;
        }
        throw new WrongNameException();
    }

    private Field checkField(UserClub clb, Sport sport, LocalDate date, int hour) throws NoFreeFieldException {
        int i = 0;
        boolean found = false;
        Club club = clb.getClub();
        Field field = null;
        while(!found && i < club.fields.size()){
            field = club.fields.get(i++);

            if(field.sport.equals(sport)){
                if(!(field.timeTable.containsKey(date))){
                    ArrayList<Integer> updatedTimes = new ArrayList<>(club.times);
                    field.timeTable.put(date, updatedTimes);
                    found = true;
                }
                else {
                    ArrayList<Integer> times = field.timeTable.get(date);
                    if(times.contains(hour)){
                        found = true;
                    }
                }
            }
        }
        if(!found)
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
        for(int k : bd.getKeySet()){
            if(k == id){
                booking = bd.getBooking(k);
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
        for(int k : bd.getKeySet()){
            Booking booking = bd.getBooking(k);
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

    void displayBlindBookings(){
        ArrayList<Integer> keys = new ArrayList<>();
        for(int k : bd.getKeySet()){
            Booking booking = bd.getBooking(k);
            if(booking instanceof BlindBooking && !((BlindBooking) booking).isFull())
                keys.add(k);
        }
        bm.displayBookings(keys);
    }

    private void checkBlindBooking(int id) throws WrongKeyException, NoFreeSpotException {
        Booking booking = bd.getBooking(id);
        if(booking == null || booking instanceof PrivateBooking)
            throw new WrongKeyException();
        if(((BlindBooking)booking).isFull())
            throw new NoFreeSpotException();
    }

    void requestSpot(User user, int id){
        try {checkBlindBooking(id);}
        catch (WrongKeyException e) {
            System.out.print("La chiave inserita non è corretta");
            return;
        }
        catch(NoFreeSpotException e) {
            System.out.println("Nessun posto libero in questa partita");
            return;
        }
        bm.addBookingPlayer(user, id);
    }

    void addFavouriteClub(User user, String clb){
        UserClub club = null;
        try {club = checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }
        bm.addUserFavouriteClub(user, club);
    }

    private void checkNumPlayers(Sport sport, ArrayList<String> winners) throws WrongNameException {
        if(sport.numPlayers / 2 != winners.size())
            throw new WrongNameException();
    }

    private void checkMatchPlayer(User user, ArrayList<User> players) throws WrongNameException {
        if(!players.contains(user))
            throw new WrongNameException();
    }

    void addMatchResult(ArrayList<String> winners, int id) {
        Booking booking = bd.getBooking(id);
        ArrayList<User> players = booking.getPlayers();
        Sport sport = booking.getField().sport;
        try{checkNumPlayers(sport, winners);}
        catch(WrongNameException e){
            System.out.println("Il numero di utenti inseriti non è corretto");
            return;
        }
        for(String w : winners) {
            User u = null;
            try{u = checkUser(w);}
            catch(WrongNameException e) {
                System.out.println("L'utente " + w + " non è registrato al servizio");
                return;
            }
            try{checkMatchPlayer(u, players);}
            catch(WrongNameException e){
                System.out.println("L'utente " + u.username + " non ha giocato questa partita");
                return;
            }
        }
        bm.addResult(sport, players, winners);
    }

    void displayUserRecord(User user) {
        bm.displayUserRecord(user);
    }

    void deleteUser(User user) throws PendingBookingException {
        try{getUserKeys(user);}
        catch(NoActiveBookingsException e) {
            bm.removeUser(user);
            return;
        }
        throw new PendingBookingException(); //TEST ME
    }
}
