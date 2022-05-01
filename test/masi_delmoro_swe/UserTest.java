package masi_delmoro_swe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private BookingManager bm;
    private Club club;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.bm = new BookingManager(monitor);
        this.club = new Club("Gracciano", 12, 8, 9, 23, 100);
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
}
