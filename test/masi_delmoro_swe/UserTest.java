package masi_delmoro_swe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private BookingManager bm;
    private Club club;
    public Sport padel;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.bm = new BookingManager(monitor);
        this.padel = new Padel();
        this.club = new Club("Gracciano", 12, 8, 9, 23, 100);
        club.addField("Padel 1", padel);
        club.subscribe(bm);
    }

    @Test
    void addFundsTest() {
        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        User mark = marco.subscribe(bm, "marcorossi");
        int expected = 100;
        mark.addFunds(expected);
        assertEquals(expected, mark.getBalance());
    }

    @Test
    void joinClubTest() {
        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        User mark = marco.subscribe(bm, "marcorossi");
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        User gigi = luigi.subscribe(bm, "gigi");
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
    void bookFieldTest() {
        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        User mark = marco.subscribe(bm, "marcorossi");
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        User gigi = luigi.subscribe(bm, "gigi");
        Person filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
        User pippo = filippo.subscribe(bm, "pippo");
        Person elisa = new Person("elisa", "landi", "elisalandi@mail.it");
        User eli = elisa.subscribe(bm, "eli");

        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(5);

        mark.bookField(padel, "Gracciano", "03/05/2022", 15);
        Map<Integer, Booking> bookings = bm.getBookings();
        assertTrue(bookings.isEmpty());

        eli.addFunds(100);
        mark.bookField(padel, "Gracciano", "03/05/2022", 15);
        bookings = bm.getBookings();
        assertEquals(1, bookings.size());
    }
}
