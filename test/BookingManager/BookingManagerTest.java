package BookingManager;

import BalanceMonitor.BalanceMonitor;
import Booking.Booking;
import Person.Person;
import Sport.Sport;
import Sport.Padel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingManagerTest {
    private BookingManager bm;
    private AbstractBookingManager bc;
    private Person marco, luigi, filippo, elisa, francesca;
    private Sport padel;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.bm = new BookingManager(monitor);
        this.bc = new BookingChecker(bm);

        this.padel = new Padel();

        marco = new Person("marco", "rossi", "marcorossi@mail.it");
        //this.mark = marco.subscribe(bm, "mark");
        luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        //this.gigi = luigi.subscribe(bm, "gigi");
        filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
        //this.pippo = filippo.subscribe(bm, "pippo");
        elisa = new Person("elisa", "landi", "elisalandi@mail.it");
        //this.eli = elisa.subscribe(bm, "eli");
        francesca = new Person("francesca", "landi", "francescalandi@mail.it");
        //this.france = francesca.subscribe(bm, "france");
    }

    @Test
    void createUser() {
        User mark = bm.createUser(marco, "mark", bc);
        assertEquals(mark, bm.bd.getUser(0));
    }

    @Test
    void removeUser() {
        User mark = bm.createUser(marco, "mark", bc);
        assertEquals(mark, bm.bd.getUser(0));
        bm.removeUser(mark);
        assertFalse(bm.bd.containsUser(mark));
    }

    @Test
    void getUsersSize() {
        bm.createUser(marco, "mark", bc);
        bm.createUser(luigi, "gigi", bc);
        bm.createUser(filippo, "pippo", bc);
        assertEquals(3, bm.getUsersSize());
    }

    @Test
    void topUpUserBalance() {
        User mark = marco.subscribe(bc, "mark");
        bm.topUpUserBalance(mark, 100);
        assertEquals(100, mark.getBalance());
    }

    @Test
    void getBooking() {
        User mark = marco.subscribe(bc, "mark");
        User gigi = luigi.subscribe(bc, "gigi");
        User pippo = filippo.subscribe(bc, "pippo");
        User eli = elisa.subscribe(bc, "eli");
        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "eli", "pippo");
        bm.getBooking(1);
    }

    @Test
    void displayBookings() {
    }

    @Test
    void addClub() {
    }

    @Test
    void getClubsSize() {
    }

    @Test
    void addClubMember() {
    }

    @Test
    void rechargeAccount() {
    }

    @Test
    void addUserFavouriteClub() {
    }

    @Test
    void addResult() {
    }

    @Test
    void releaseField() {
    }

    @Test
    void displayUserRecord() {
    }

    @Test
    void payJoinClub() {
    }

    @Test
    void addBookingPlayer() {
    }

    @Test
    void releaseSpot() {
    }

    @Test
    void createBooking() {
    }

    @Test
    void createBlindBooking() {
    }
}