package BookingManager;

import BalanceMonitor.BalanceMonitor;
import Club.Club;
import Person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserClubTest {
    private Club clb1, clb2, clb3, clb4, clb5;
    private UserClub userClub1, userClub2, userClub3, userClub4, userClub5;
    private User mark, gigi, pippo;
    private BookingManager manager;
    private AbstractBookingManager bm;

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

        this.userClub1 = clb1.subscribe(bm, 10, 100);
        this.userClub2 = clb2.subscribe(bm, 20, 200);
        this.userClub3 = clb3.subscribe(bm, 7, 90);
        this.userClub4 = clb4.subscribe(bm, 15, 100);
        this.userClub5 = clb5.subscribe(bm, 12, 150);

        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        this.mark = marco.subscribe(bm, "mark");
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        this.gigi = luigi.subscribe(bm, "gigi");
        Person filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
        this.pippo = filippo.subscribe(bm, "pippo");
    }

    @Test
    void getClub() {
        Club club1 = userClub1.getClub();
        Club club2 = userClub2.getClub();
        Club club3 = userClub3.getClub();
        Club club4 = userClub4.getClub();
        Club club5 = userClub5.getClub();
        assertEquals(clb1, club1);
        assertEquals(clb2, club2);
        assertEquals(clb3, club3);
        assertEquals(clb4, club4);
        assertEquals(clb5, club5);
    }

    @Test
    void isMember() {
        mark.addFunds(200);
        mark.joinClub("Gracciano");
        assertTrue(userClub2.isMember(mark));
    }

    @Test
    void addMember() {
        userClub1.addMember(mark);
        assertTrue(userClub1.isMember(mark));
    }


}