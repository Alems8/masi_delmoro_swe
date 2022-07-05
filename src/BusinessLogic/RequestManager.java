package BusinessLogic;


import DomainModel.Club;
import DomainModel.UserClub;
import DomainModel.Sport;
import DomainModel.User;
import DomainModel.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RequestManager {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final ClubController cc;
    final UserController uc;
    final BookingController bc;
    private static RequestManager instance = null;

    private RequestManager(){
        this.cc = new ClubController();
        this.uc = new UserController();
        this.bc = new BookingController(cc, uc);

    }

    public static RequestManager getInstance() {
        if(instance == null)
            instance = new RequestManager();
        return instance;
    }

    public void requestBooking(Sport sport, String clb, String day, int hour,
                               ArrayList<String> users) {
        LocalDate date = LocalDate.parse(day, dtf);

        try{cc.setCurrentClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al " +
                    "servizio");
            return;
        }

        try{cc.findField(sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }

        int size = sport.numPlayers;
        try{uc.setCurrentPlayers(users, size);}
        catch (NoFreeSpotException e){
            System.out.println("Il numero di giocatori è sbagliato");
            return;
        }
        catch (WrongNameException e){
            System.out.println("Uno degli utenti selezionati non esiste");
            return;
        }
        bc.createBooking();
    }

    public User addUser(Person person, String username){
        return uc.createUser(person, username, this);
    }

    public UserClub addClub(Club club, int joinClubPrice){
        return cc.addClub(club, joinClubPrice);
    }

    public void topUpBalance(User user, int money){
        uc.topUpUserBalance(user, money);
    }

    public void requestBlindBooking(Sport sport, String clb, String day, int hour, User user) {
        LocalDate date = LocalDate.parse(day, dtf);

        try{cc.setCurrentClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }

        try{cc.findField(sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }

        uc.setCurrentUser(user);

        bc.createBlindBooking();
    }

    public void requestSpot(User user, int id) {
        try{bc.setCurrentBooking(id);}
        catch (WrongKeyException e) {
            System.out.println("La chiave inserita non è corretta");
            return;
        }
        uc.setCurrentUser(user);
        bc.addBookingPlayer();
    }

    public void requestJoinClub(User user, String clb) {
        try{cc.setCurrentClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non è iscritto al servizio");
            return;
        }
        UserClub club = cc.getCurrentClub();
        uc.setCurrentUser(user);
        try{
            uc.payJoining(club);
            cc.addClubMember(user);
        }
        catch(LowBalanceException e){
            System.out.println("Non hai credito sufficiente");
            return;
        }
        catch(AlreadySubscribedException e) {
            System.out.println("Sei già associato a questo club");
            int price = club.joinClubPrice;
            uc.refund(price);
            return;
        }
    }

    public void deleteUserBooking(User user, int id) {
        try{bc.setCurrentBooking(id);}
        catch (WrongKeyException e) {
            System.out.println("La chiave inserita non è corretta");
            return;
        }
        uc.setCurrentUser(user);
        try{bc.deleteUserBooking();}
        catch(WrongKeyException e) {
            System.out.println("Non hai diritti su questa partita");
            return;
        }
    }

    public void displayUserBookings(User user) {
        uc.setCurrentUser(user);
        try{bc.displayUserBookings();}
        catch (NoActiveBookingsException e) {
            System.out.println("Non hai prenotazioni attive");
        }
    }

    public void displayBlindBookings(Sport sport) {
        try{bc.displayBlindBookings(sport);}
        catch(NoActiveBookingsException e) {
            System.out.println("Non ci sono partite disponibili");
        }
    }

    public void addMatchResult(ArrayList<String> winners, int id) {
        try{bc.setCurrentBooking(id);}
        catch (WrongKeyException e) {
            System.out.println("La chiave inserita non è corretta");
            return;
        }
        try{uc.setCurrentPlayers(winners, bc.getCurrentBooking().getPlayers().size()/2);}
        catch (NoFreeSpotException e){
            System.out.println("Il numero di giocatori è sbagliato");
            return;
        }
        catch (WrongNameException e){
            System.out.println("Uno degli utenti selezionati non esiste");
            return;
        }

        bc.addMatchResult();
    }

    public void displayUserRecord(User user) {
        uc.setCurrentUser(user);
        bc.displayUserRecord();
    }

    public void deleteUser(User user) {
        uc.setCurrentUser(user);
        try{bc.checkActiveBookings();}
        catch (PendingBookingException e) {
            System.out.println("Hai delle prenotazioni in sospeso");
            return;
        }
        uc.deleteUser();
    }

    public void addFavouriteClub(User user, String club) {
        try{cc.setCurrentClub(club);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }
        uc.setCurrentUser(user);
        uc.addFavouriteClub(cc.getCurrentClub());
    }
}
