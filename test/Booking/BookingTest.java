package Booking;

import BalanceMonitor.BalanceMonitor;
import BookingManager.AbstractBookingManager;
import BookingManager.BookingManager;
import BookingManager.BookingChecker;
import BookingManager.User;
import Club.Club;
import Person.Person;
import Sport.Sport;
import Sport.Padel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    private Sport padel = new Padel();
    private Club clb1, clb2, clb3, clb4, clb5;
    private User mark, gigi, pippo, eli, france;
    private BookingManager manager;
    private AbstractBookingManager bm;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.manager = new BookingManager(monitor);
        this.bm = new BookingChecker(manager);
        this.clb1 = new Club("LaFiorita", 15, 9,19);
        this.clb2 = new Club("Gracciano", 30, 10,23);
        this.clb3 = new Club("UPP", 10, 7,22);
        this.clb4 = new Club("Certaldo", 20, 8,20);
        this.clb5 = new Club("Firenze Padel", 18, 9,18);

        clb1.subscribe(bm, 10, 100);
        clb2.subscribe(bm, 20, 200);
        clb3.subscribe(bm, 7, 90);
        clb4.subscribe(bm, 15, 100);
        clb5.subscribe(bm, 12, 150);

        clb2.addField("Padel 1", padel);

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

        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
    }

    @Test
    void getClub() {
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");
        Booking booking = manager.getBooking(1);
        assertEquals(clb2, booking.getClub());
    }

    @Test
    void getField() {
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");
        Booking booking = manager.getBooking(1);
        assertEquals(clb2.fields.get(0), booking.getField());
    }

    @Test
    void getDate() {
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");
        Booking booking = manager.getBooking(1);
        assertEquals("12/05/2022", booking.getDate().format(dtf));
    }

    @Test
    void getHour() {
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");
        Booking booking = manager.getBooking(1);
        assertEquals(10, booking.getHour());
    }

    @Test
    void getPlayers() {
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");
        Booking booking = manager.getBooking(1);
        assertEquals(mark, booking.getPlayers().get(0));
        assertEquals(gigi, booking.getPlayers().get(1));
        assertEquals(pippo, booking.getPlayers().get(2));
        assertEquals(eli, booking.getPlayers().get(3));
    }

    @Test
    void containsUser() {
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");
        Booking booking = manager.getBooking(1);
        assertTrue(booking.containsUser(mark));
        assertTrue(booking.containsUser(gigi));
        assertFalse(booking.containsUser(france));
    }
}