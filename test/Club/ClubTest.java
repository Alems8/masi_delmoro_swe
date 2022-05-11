package Club;

import BalanceMonitor.BalanceMonitor;
import BookingManager.BookingManager;
import BookingManager.AbstractBookingManager;
import BookingManager.BookingChecker;
import BookingManager.User;
import Person.Person;
import Sport.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClubTest {
    private Club clb1, clb2, clb3, clb4, clb5;
    private Sport padel, soccer;
    private User mark, gigi, pippo;
    private AbstractBookingManager bm;
    private BookingManager manager;


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

    }
    @Test
    void subscribe() {
        clb1.subscribe(bm, 10, 100);
        clb2.subscribe(bm, 20, 200);
        clb3.subscribe(bm, 7, 90);
        clb4.subscribe(bm, 15, 100);
        clb5.subscribe(bm, 12, 150);
        assertEquals(5, manager.getClubsSize());
    }

    @Test
    void addField() {
        clb1.addField("Padel 1", padel);
        clb1.addField("Soccer 1", soccer);
        clb2.addField("Padel 1", padel);
        clb2.addField("Padel 2", padel);
        assertEquals(2, clb1.fields.size());
        assertEquals(2, clb1.fields.size());
    }
}