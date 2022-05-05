package BookingManager;

import BalanceMonitor.BalanceMonitor;
import Booking.Booking;

import Club.Club;
import Booking.BlindBooking;
import Sport.Sport;
import Sport.Padel;

import Person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private BookingManager bm;
    private Club club;
    public Sport padel;
    private User mark;
    private User gigi;
    private User pippo;
    private User eli;
    private User france;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.bm = new BookingManager(monitor);

        this.padel = new Padel();
        this.club = new Club("Gracciano", 12, 8, 9, 23, 100);
        club.addField("Padel 1", padel);
        club.subscribe(bm);

        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        this.mark = marco.subscribe(bm, "mark");
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        this.gigi = luigi.subscribe(bm, "gigi");
        Person filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
        this.pippo = filippo.subscribe(bm, "pippo");
        Person elisa = new Person("elisa", "landi", "elisalandi@mail.it");
        this.eli = elisa.subscribe(bm, "eli");
        Person francesca = new Person("francesca", "landi", "francescalandi@mail.it");
        this.france = francesca.subscribe(bm, "france");
    }

    @Test
    void addFundsTest() {
        int expected = 100;
        mark.addFunds(expected);
        assertEquals(expected, mark.getBalance());
    }

    @Test
    void joinClubTest() {
        mark.addFunds(90);
        mark.joinClub("Gracciano");
        assertFalse(club.isMember(mark));

        mark.addFunds(10);
        gigi.addFunds(150);
        mark.joinClub("Gracciano");
        gigi.joinClub("Gracciano");
        assertTrue(club.isMember(mark));
        assertTrue(club.isMember(gigi));
    }

    @Test
    void bookFieldTest() throws LowBalanceException {

        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(5);

        mark.bookField(padel, "Gracciano", "03/05/2022", 15, "gigi", "eli", "pippo");
        Map<Integer, Booking> bookings = bm.getBookings();
        assertTrue(bookings.isEmpty());

        eli.addFunds(100);
        mark.bookField(padel, "Gracciano", "03/05/2022", 15,"gigi","eli","pippo");
        bookings = bm.getBookings();
        assertEquals(1, bookings.size());
    }
    @Test
    void deleteBookingTest() throws LowBalanceException {
        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        mark.bookField(padel, "Gracciano", "03/05/2022", 15,"gigi","eli","pippo");
        Map<Integer, Booking> bookings = bm.getBookings();
        assertEquals(1, bookings.size());
        mark.deleteBooking(1);
        assertEquals(0, bookings.size());
    }

    @Test
    void blindBookTest(){
        mark.addFunds(100);
        mark.blindBook(padel,"Gracciano","04/05/2022",15);
        Map<Integer, Booking> bookings = bm.getBookings();
        assertTrue(bookings.get(1) instanceof BlindBooking);
    }

    @Test
    void bookSpotTest(){
        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        france.addFunds(100);
        mark.blindBook(padel,"Gracciano","04/05/2022",15);
        Map<Integer, Booking> bookings = bm.getBookings();

        gigi.bookSpot(1);
        assertEquals(2,bookings.get(1).getPlayers().size());
        pippo.bookSpot(1);
        assertEquals(3,bookings.get(1).getPlayers().size());
        eli.bookSpot(1);
        assertEquals(4,bookings.get(1).getPlayers().size());
        france.bookSpot(1);
        assertEquals(4,bookings.get(1).getPlayers().size());
    }

    @Test
    void addFavouriteClubTest(){
        mark.addFavouriteClub("Gracciano");
        assertEquals(mark.getFavouriteClubs().get(0), club);
    }

    @Test
    void addMatchResultTest() throws LowBalanceException {
        mark.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        gigi.addFunds(100);
        mark.bookField(padel, "Gracciano", "06/05/2022", 12, "eli", "pippo", "gigi");
        mark.viewBookings();
        mark.addMatchResult("mark", "eli", 1);
        assertEquals(1, mark.record[1]);
        assertEquals(0, mark.record[0]);
        assertEquals(1, eli.record[1]);
        assertEquals(0, eli.record[0]);
        assertEquals(1, gigi.record[0]);
        assertEquals(0, gigi.record[1]);
        assertEquals(1, pippo.record[0]);
        assertEquals(0, pippo.record[1]);
    }

    @Test
    void deleteAccountTest() {
        mark.deleteAccount();
        assertFalse(bm.getUsers().contains(mark));
    }
}