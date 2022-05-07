package BookingManager;

import Booking.Booking;
import Booking.PrivateBooking;
import Booking.BlindBooking;
import Club.Field;
import Sport.Sport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserInterface {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  //TODO FIXME
    private BookingManager bm;


    public UserInterface(BookingManager bm){
        this.bm = bm;
    }



    public void requestJoinClub(User user, String clb){
        UserClub club = null;
        try{club = bm.checkClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non è iscritto al servizio");
            return;
        }
        if(club.isMember(user)) {
            System.out.println("Sei già iscritto al club");
            return;
       }

        try{bm.payJoinClub(user, club);}
        catch (LowBalanceException ex) {
            System.out.println("Non hai abbastanza credito per associarti al club");
            return;
        }
        club.addMember(user);

    }
    public void addUserFavouriteClub(User user, String clb) {
        UserClub club = null;
        try {club = bm.checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }
        user.getFavouriteClubs().add(club);
    }



    public void requestBooking(Sport sport, String clb, String day, int hour, ArrayList<String> users) {
        LocalDate date = LocalDate.parse(day, dtf);
        UserClub club = null;
        try{club = bm.checkClub(clb);}
        catch (WrongNameException e){
            System.out.println("Il club non esiste o non è registrato al servizio");
            return;
        }

        Field field = null;
        try{field = bm.checkField(club, sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }

        int size = field.sport.numPlayers;
        ArrayList<User> players = new ArrayList<>();
        for(int i=0; i<size; i++){
            User u = null;
            try {u = bm.checkUser(users.get(i));}
            catch(WrongNameException e) {
                field.timeTable.get(date).add(hour);
                System.out.println("L'utente inserito non esiste");
                return;
            }

            try{bm.pay(u, club);}
            catch(LowBalanceException e){
                field.timeTable.get(date).add(hour);
                System.out.println("L'utente non ha credito sufficiente");
                return;
            }
            players.add(u);
        }

        bm.createBooking( new PrivateBooking(club.getClub(), field, date, hour, players));
    }
    public void deleteUserBooking(User user, int id) {
        Booking booking = null;
        try {booking = bm.checkBooking(user, id);}
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
    public void displayUserBookings(User user) {
        ArrayList<Integer> keys = new ArrayList<>();
        try{keys = bm.getUserKeys(user);}
        catch(NoActiveBookingsException e) {
            System.out.println("Non hai nessuna prenotazione");
        }
        for(int k : keys){
            System.out.println(k+bm.searchBooking(k).toString()); //TODO OPPURE CHIAMA DIRETTAMENTE BD?
        }
    }





    public void requestBlindBooking(Sport sport, String clb, String day, int hour, User user) {
        LocalDate date = LocalDate.parse(day, dtf);
        UserClub club = null;
        try {club = bm.checkClub(clb);}
        catch (WrongNameException e) {
            System.out.println("Il club inserito non è iscritto al servizio");
            return;
        }
        Field field = null;
        try{field = bm.checkField(club, sport, date, hour);}
        catch(NoFreeFieldException e) {
            System.out.println("Nessun campo disponibile");
            return;
        }
        ArrayList<User> players = new ArrayList<>();
        players.add(user);
        bm.createBooking(new BlindBooking(club.getClub(), field, date, hour, players));
    }
    public void requestSpot(User user, int id){
        try{bm.checkBlindBooking(id);}
        catch(WrongKeyException e) {
            System.out.print("Non puoi prenotare un posto in questa partita");
            return;
        }
        Booking booking = bm.searchBooking(id); //TODO OPPURE CHIAMA DIRETTAMENTE BD?
        ((BlindBooking) booking).addPlayer(user);
    }
    public void displayBlindBookings(){
        for(int k : bm.getKeys()){ //TODO OPPURE CHIAMA DIRETTAMENTE BD?
            Booking booking = bm.searchBooking(k); //TODO OPPURE CHIAMA DIRETTAMENTE BD?
            if(booking instanceof BlindBooking && !((BlindBooking) booking).isFull())
                System.out.println(k+booking.toString());
        }
    }





    public void addResult(ArrayList<String> winners, int id) throws WrongNameException {
        Booking booking = bm.deleteBooking(id); //TODO OPPURE CHIAMA DIRETTAMENTE BD?
        ArrayList<User> players = booking.getPlayers();
        Sport sport = booking.getField().sport;
        if(sport.numPlayers / 2 != winners.size())
            throw new WrongNameException();
        for(String w : winners) {
            if(!players.contains(bm.checkUser(w)))
                throw new WrongNameException();
        }
        for (User u : players){
            if(u.record.containsKey(sport))
                u.record.get(sport)[0]++;
            else {
                int[] result = new int[2];
                u.record.put(sport, result);
                u.record.get(sport)[0]++;
            }
            for(String w : winners){
                if(w.equals(u.username)){
                    u.record.get(sport)[1]++;
                    u.record.get(sport)[0]--;
                }
            }
        }


    }
    public void displayUserRecord(User user){

        System.out.println("Il tuo storico è: ");
        for(Sport k : user.record.keySet()){
            System.out.println(k.name + ": " + user.record.get(k)[1] + " vittorie - " + user.record.get(k)[0] +
                    " sconfitte");
        }
    }


    public void deleteUser(User user) throws PendingBookingException {
        try{bm.getUserKeys(user);}
        catch(NoActiveBookingsException e) {
            bm.removeUser(user); //TODO OPPURE CHIAMA DIRETTAMENTE BD?
            System.out.println("Utente rimosso correttamente");
            return;
        }
        throw new PendingBookingException();
    }
    public void rechargeAccount(User user, int money){
        user.setBalance(user.getBalance() + money);
    }
}
