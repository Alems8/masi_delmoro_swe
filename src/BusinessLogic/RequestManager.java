package BusinessLogic;


import Club.Club;
import Club.Field;
import Club.UserClub;
import Sport.Sport;
import User.User;
import User.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RequestManager {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ClubController cc;
    private final UserController uc;
    private final BookingController bc;

    public RequestManager(){
        this.cc = new ClubController();
        this.uc = new UserController();
        this.bc = new BookingController(cc, uc);

    }

    public void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users) {
        LocalDate date = LocalDate.parse(day, dtf);

        try{cc.setCurrentClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }

        try{cc.setCurrentField(sport, date, hour);}
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

    public User addUser(Person person, String username) throws WrongNameException, AlreadySubscribedException {
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

        try{cc.setCurrentField(sport, date, hour);}
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

    public void requestJoinClub(User user, String club) { //FIX ME
        try{cc.setCurrentClub(club);}
        catch (WrongNameException e){
            System.out.println("Il club non è iscritto al servizio");
            return;
        }
        uc.setCurrentUser(user);
        try{
            uc.payJoining();
            cc.addClubMember(user);
        }
        catch(AlreadySubscribedException e) {
            System.out.println("Sei già associato a questo club");
            return;
        }
    }
}
