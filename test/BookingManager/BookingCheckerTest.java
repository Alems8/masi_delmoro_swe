package BookingManager;

import BalanceMonitor.BalanceMonitor;
import Booking.BlindBooking;
import Club.Club;
import Person.Person;
import Sport.Padel;
import Sport.Soccer;
import Sport.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Sport soccer = new Soccer();
        this.padel = new Padel();

        this.club = new Club("Firenze Padel", 18, 9,18);
        club.addField("Soccer 1", soccer);
        club.addField("Padel 2", padel);
        this.userClub = club.subscribe(bc, 15,100 );
    }

    @Test
    void addUser() throws WrongNameException, AlreadySubscribedException {
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        bc.addUser(mattia, "matti");
        assertEquals(5,bm.bd.getUsersSize());

        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> bc.addUser(luigi, "matti")
        );

        AlreadySubscribedException thrown2 = assertThrows(
                AlreadySubscribedException.class,
                () -> bc.addUser(mattia, "gigi")
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));
    }

    @Test
    void addClub() {
        Club clb = new Club("Gracciano", 15, 9,18);
        bc.addClub(clb, 15, 100);
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
        bc.requestBlindBooking(padel,"Firenze Padel", "11/05/2022",14, mark);
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

   /* @Test
    void checkUser() {
        bc.checkUser("mark");
    }*/
}