package BusinessLogic;

import Club.Club;
import Club.Field;
import User.Person;
import Sport.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


class BookingCheckerTest {
    private AbstractBookingManager bc;
    private BookingManager bm;
    private Person mattia;
    private User mark;
    private User gigi;
    private User pippo;
    private User eli;
    private Sport padel;
    private Sport soccer;
    private Club club;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.bm = new BookingManager(monitor);
        this.bc = new BookingChecker(bm);

        this.mattia = new Person("mattia", "verdi", "mattiaverdi@mail.it");
        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        this.mark = marco.subscribe(bc, "mark");
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        this.gigi = luigi.subscribe(bc, "gigi");
        Person filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
        this.pippo = filippo.subscribe(bc, "pippo");
        Person elisa = new Person("elisa", "landi", "elisalandi@mail.it");
        this.eli = elisa.subscribe(bc, "eli");
        this.soccer = new Soccer();
        this.padel = new Padel();

        this.club = new Club("Firenze Padel", 9, 18,3);
        club.addField("Padel 2", padel,15);
        this.userClub = club.subscribe(bc, 100 );
    }

    @Test
    void addUser() throws WrongNameException, AlreadySubscribedException {
        Person luigi = new Person("luigi", "bianchi","luigibianchi@mail.it");
        bc.addUser(mattia, "matti");
        assertEquals(5,bm.bd.getUsersSize());

        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> bc.addUser(luigi, "matti")
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));

        AlreadySubscribedException thrown2 = assertThrows(
                AlreadySubscribedException.class,
                () -> bc.addUser(mattia, "gigi")
        );
        assertTrue(thrown2.getMessage().contains("Utente già registrato"));
    }

    @Test
    void addClub() {
        Club clb = new Club("Gracciano", 9, 16,8);
        bc.addClub(clb, 100);
        assertEquals(2,bm.bd.getClubsSize());
    }

    @Test
    void requestBooking() {
        ArrayList<String> users = new ArrayList<>();
        users.add("mark");
        users.add("gigi");
        users.add("pippo");
        users.add("eli");

        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);

        bc.requestBooking(padel, "Firenze Padel", "11/05/2022",15,users);

        assertEquals(1,bm.bd.getBookingsSize());
    }

    @Test
    void deleteUserBooking() {
        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        ArrayList<String> users = new ArrayList<>();
        users.add("mark");
        users.add("gigi");
        users.add("pippo");
        users.add("eli");

        bc.requestBooking(padel,"Firenze Padel", "11/05/2022",15, users);
        bc.requestBlindBooking(padel,"Firenze Padel", "11/05/2022",14, mark);

        bc.deleteUserBooking(mark,1);
        assertEquals(1, bm.bd.getBookingsSize());

        bc.deleteUserBooking(mark,2);
        assertEquals(0,bm.bd.getBookingsSize());
    }

    @Test
    void requestJoinClub() {
        mark.addFunds(150);
        bc.requestJoinClub(mark, "Firenze Padel");
        assertTrue(userClub.isMember(mark));
    }

    @Test
    void topUpBalance() {
        bc.topUpBalance(mark,100);
        assertEquals(100, mark.getBalance());
    }

    @Test
    void requestBlindBooking() {
        mark.addFunds(100);
        bc.requestBlindBooking(padel,"Firenze Padel", "12/05/2022",14, mark);
        assertEquals(1, bm.bd.getBookingsSize());
        assertTrue(bm.bd.getBooking(1) instanceof BlindBooking);

    }

    @Test
    void requestSpot() {
        mark.addFunds(100);
        gigi.addFunds(100);
        eli.addFunds(100);
        pippo.addFunds(100);
        bc.requestBlindBooking(padel,"Firenze Padel", "11/05/2022",14, mark);
        assertTrue(bm.bd.getBooking(1) instanceof BlindBooking);

        bc.requestSpot(gigi, 1);
        assertEquals(2, bm.bd.getBooking(1).getPlayers().size());
        bc.requestSpot(pippo, 1);
        assertEquals(3, bm.bd.getBooking(1).getPlayers().size());
        bc.requestSpot(eli, 1);
        assertEquals(4, bm.bd.getBooking(1).getPlayers().size());

        assertTrue(((BlindBooking) bm.bd.getBooking(1)).isFull());
    }

    @Test
    void addFavouriteClub() {
        bc.addFavouriteClub(mark, "Firenze Padel");
        assertTrue(mark.getFavouriteClubs().contains(userClub));
    }

    @Test
    void addMatchResult() {
        ArrayList<String> users = new ArrayList<>();
        users.add("mark");
        users.add("gigi");
        users.add("pippo");
        users.add("eli");
        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        bc.requestBooking(padel, "Firenze Padel", "11/05/2022",15,users);

        ArrayList<String> winners = new ArrayList<>();
        winners.add("mark");
        winners.add("eli");

        bc.addMatchResult(winners,1);

        assertEquals(0,mark.record.get(padel)[0]);
        assertEquals(1,mark.record.get(padel)[1]);
        assertEquals(0,eli.record.get(padel)[0]);
        assertEquals(1,eli.record.get(padel)[1]);
        assertEquals(1,pippo.record.get(padel)[0]);
        assertEquals(0,pippo.record.get(padel)[1]);
        assertEquals(1,gigi.record.get(padel)[0]);
        assertEquals(0,gigi.record.get(padel)[1]);


    }

    @Test
    void deleteUser() throws PendingBookingException {
        mark.addFunds(100);
        bc.requestBlindBooking(padel, "Firenze Padel", "11/05/2022", 14, mark);

        PendingBookingException thrown = assertThrows(
                PendingBookingException.class,
                () -> bc.deleteUser(mark)
        );
        assertTrue(thrown.getMessage().contains("Hai delle prenotazioni in sospeso"));

        bc.deleteUserBooking(mark,1);
        assertEquals(mark, bm.bd.getUser(0));
        bc.deleteUser(mark);
        assertEquals(gigi, bm.bd.getUser(0));
    }

    @Test
    void checkUser() throws WrongNameException {
        User u =((BookingChecker)bc).checkUser("mark");
        assertEquals(mark, u);

        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> ((BookingChecker)bc).checkUser("marco")
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));
    }

    @Test
    void checkBalance() throws LowBalanceException {
        LowBalanceException thrown = assertThrows(
                LowBalanceException.class,
                () -> ((BookingChecker)bc).checkBalance(mark,userClub, club.fields.get(0))
        );
        assertTrue(thrown.getMessage().contains("Non hai abbastanza fondi per una prenotazione"));
        bc.topUpBalance(mark,100);
        ((BookingChecker)bc).checkBalance(mark, userClub, club.fields.get(0));
    }

    @Test
    void checkClub() throws WrongNameException {
        UserClub uc = ((BookingChecker)bc).checkClub("Firenze Padel");
        assertEquals(userClub, uc);

        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> ((BookingChecker)bc).checkClub("Firenze")
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));
    }

    @Test
    void checkField() throws NoFreeFieldException {
        Field field = ((BookingChecker)bc).checkField(userClub,padel, LocalDate.parse("12/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15);
        assertTrue(club.fields.contains(field));

        NoFreeFieldException thrown = assertThrows(
                NoFreeFieldException.class,
                () -> ((BookingChecker)bc).checkField(userClub,padel, LocalDate.parse("12/05/2022",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy") ),20)
        );
        assertTrue(thrown.getMessage().contains("Non ci sono campi disponibili"));

        NoFreeFieldException thrown2 = assertThrows(
                NoFreeFieldException.class,
                () -> ((BookingChecker)bc).checkField(userClub,soccer, LocalDate.parse("12/05/2022",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15)
        );
        assertTrue(thrown2.getMessage().contains("Non ci sono campi disponibili"));

    }

    @Test
    void checkBooking() throws WrongKeyException {
        bc.topUpBalance(mark,100);
        bc.requestBlindBooking(padel, "Firenze Padel", "12/05/2022",15, mark);
        Booking booking = ((BookingChecker)bc).checkBooking(mark,1);
        assertEquals(booking,bc.bd.getBooking(1));

        WrongKeyException thrown = assertThrows(
                WrongKeyException.class,
                () -> ((BookingChecker)bc).checkBooking(mark,2)
        );
        assertTrue(thrown.getMessage().contains("La chiave della prenotazione è sbagliata"));

        WrongKeyException thrown2 = assertThrows(
                WrongKeyException.class,
                () -> ((BookingChecker)bc).checkBooking(eli,1)
        );
        assertTrue(thrown2.getMessage().contains("La chiave della prenotazione è sbagliata"));
    }

    @Test
    void checkJoinClubBalance(){
        LowBalanceException thrown = assertThrows(
                LowBalanceException.class,
                () -> ((BookingChecker)bc).checkJoinClubBalance(mark, userClub)
        );
        assertTrue(thrown.getMessage().contains("Non hai abbastanza fondi per una prenotazione"));
    }

    @Test
    void getUserKeys() throws NoActiveBookingsException {
        NoActiveBookingsException thrown = assertThrows(
                NoActiveBookingsException.class,
                () -> ((BookingChecker)bc).getUserKeys(mark)
        );
        assertTrue(thrown.getMessage().contains("L'utente non ha prenotazioni attive"));

        bc.topUpBalance(mark, 100);
        bc.requestBlindBooking(padel, "Firenze Padel", "12/05/2022",15, mark);
        ArrayList<Integer> keySet = ((BookingChecker)bc).getUserKeys(mark);
        assertEquals(1, keySet.size());
        assertTrue(keySet.contains(1));
    }

    @Test
    void checkBlindBooking() throws WrongKeyException, NoFreeSpotException {
        mark.addFunds(100);
        gigi.addFunds(100);
        eli.addFunds(100);
        pippo.addFunds(100);

        bc.requestBlindBooking(padel,"Firenze Padel", "12/05/2022",14, mark);
        bc.requestSpot(gigi,1);
        bc.requestSpot(eli,1);

        ((BookingChecker)bc).checkBlindBooking(1);

        bc.requestSpot(pippo,1);
        NoFreeSpotException thrown = assertThrows(
                NoFreeSpotException.class,
                () -> ((BookingChecker)bc).checkBlindBooking(1)
        );
        assertTrue(thrown.getMessage().contains("Nessun posto disponibile"));

        WrongKeyException thrown2 = assertThrows(
                WrongKeyException.class,
                () -> ((BookingChecker)bc).checkBlindBooking(2)
        );
        assertTrue(thrown2.getMessage().contains("La chiave della prenotazione è sbagliata"));

        ArrayList<String> users = new ArrayList<>();
        users.add("mark");
        users.add("gigi");
        users.add("pippo");
        users.add("eli");
        bc.requestBooking(padel, "Firenze Padel", "11/05/2022",15,users);

        WrongKeyException thrown3 = assertThrows(
                WrongKeyException.class,
                () -> ((BookingChecker)bc).checkBlindBooking(2)
        );
        assertTrue(thrown3.getMessage().contains("La chiave della prenotazione è sbagliata"));
    }

    @Test
    void checkNumPlayers() throws WrongNameException {
        ArrayList<String> winners = new ArrayList<>();
        winners.add("mark");

        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> ((BookingChecker)bc).checkNumPlayers(padel,winners)
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));

        winners.add("eli");
        ((BookingChecker)bc).checkNumPlayers(padel,winners);

        WrongNameException thrown2 = assertThrows(
                WrongNameException.class,
                () -> ((BookingChecker)bc).checkNumPlayers(soccer,winners)
        );
        assertTrue(thrown2.getMessage().contains("Il nome inserito non è coretto"));
    }

    @Test
    void checkMatchPlayer() throws WrongNameException {
        ArrayList<User> players = new ArrayList<>();
        players.add(mark);

        ((BookingChecker)bc).checkMatchPlayer(mark,players);

        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> ((BookingChecker)bc).checkMatchPlayer(eli,players)
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));

    }
}