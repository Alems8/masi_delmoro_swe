package BusinessLogic;

import Club.Club;
import Sport.Sport;

import User.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private AbstractBookingManager bm;
    private UserClub club;
    public Sport padel;
    private User mark;
    private User gigi;
    private User pippo;
    private User eli;
    private User france;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        BookingManager manager = new BookingManager(monitor);
        this.bm = new BookingChecker(manager);

        this.padel = new Padel();
        Club club = new Club("Gracciano", 2, 18, 3);
        club.addField("Padel 1", padel,15);
        this.club = club.subscribe(bm, 100);

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
    void bookFieldTest()  {

        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(5);

        mark.bookField(padel, "Gracciano", "03/05/2022", 15, "gigi", "eli", "pippo");
        assertEquals(0, bm.bd.getBookingsSize());

        eli.addFunds(100);
        mark.bookField(padel, "Gracciano", "03/05/2022", 15,"gigi","eli","pippo");
        assertEquals(1, bm.bd.getBookingsSize());
    }
    @Test
    void deleteBookingTest()  {
        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        mark.bookField(padel, "Gracciano", "03/05/2022", 15,"gigi","eli","pippo");
        assertEquals(1, bm.bd.getBookingsSize());
        mark.deleteBooking(1);
        assertEquals(0, bm.bd.getBookingsSize());
    }

    @Test
    void blindBookTest(){
        mark.addFunds(100);
        mark.blindBook(padel,"Gracciano","04/05/2022",15);
        assertTrue(bm.bd.getBooking(1) instanceof BlindBooking);
    }

    @Test
    void bookSpotTest(){
        mark.addFunds(100);
        gigi.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        france.addFunds(100);
        mark.blindBook(padel,"Gracciano","04/05/2022",15);

        gigi.bookSpot(1);
        assertEquals(2,bm.bd.getBooking(1).getPlayers().size());
        pippo.bookSpot(1);
        assertEquals(3,bm.bd.getBooking(1).getPlayers().size());
        eli.bookSpot(1);
        assertEquals(4,bm.bd.getBooking(1).getPlayers().size());
        france.bookSpot(1);
        assertEquals(4,bm.bd.getBooking(1).getPlayers().size());
    }

    @Test
    void addFavouriteClubTest(){
        mark.addFavouriteClub("Gracciano");
        assertEquals(mark.getFavouriteClubs().get(0), club);
    }

    @Test
    void addMatchResultTest()  {
        mark.addFunds(100);
        pippo.addFunds(100);
        eli.addFunds(100);
        gigi.addFunds(100);
        mark.bookField(padel, "Gracciano", "06/05/2022", 12, "eli", "pippo", "gigi");
        mark.viewBookings();
        mark.addMatchResult("mark", "eli", 1);
        assertEquals(1, mark.record.get(padel)[1]);
        assertEquals(0, mark.record.get(padel)[0]);
        assertEquals(1, eli.record.get(padel)[1]);
        assertEquals(0, eli.record.get(padel)[0]);
        assertEquals(1, gigi.record.get(padel)[0]);
        assertEquals(0, gigi.record.get(padel)[1]);
        assertEquals(1, pippo.record.get(padel)[0]);
        assertEquals(0, pippo.record.get(padel)[1]);
    }

    @Test
    void deleteAccountTest() {
        mark.deleteAccount();
        assertFalse(bm.bd.containsUser(mark));
    }
}