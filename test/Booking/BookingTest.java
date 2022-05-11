package Booking;

import BalanceMonitor.BalanceMonitor;
import BookingManager.AbstractBookingManager;
import BookingManager.BookingManager;
import BookingManager.BookingChecker;
import BookingManager.User;
import BookingManager.UserClub;
import Club.Club;
import Person.Person;
import Sport.Sport;
import Sport.Padel;
import Sport.Soccer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    private Sport padel = new Padel();
    private Sport soccer = new Soccer();
    private Club clb1, clb2, clb3, clb4, clb5;
    private UserClub userClub1, userClub2, userClub3, userClub4, userClub5;
    private User mark, gigi, pippo, eli, france;
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
        Person elisa = new Person("elisa", "landi", "elisalandi@mail.it");
        this.eli = elisa.subscribe(bm, "eli");
        Person francesca = new Person("francesca", "landi", "francescalandi@mail.it");
        this.france = francesca.subscribe(bm, "france");

        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "pippo", "eli");

    }

    @Test
    void getClub() {

    }

    @Test
    void getField() {
    }

    @Test
    void getDate() {
    }

    @Test
    void getHour() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void containsUser() {
    }
}