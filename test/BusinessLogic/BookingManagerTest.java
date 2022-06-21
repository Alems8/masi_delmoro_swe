package BusinessLogic;

import Club.Club;
import User.Person;
import Club.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookingManagerTest {
    private BookingManager bm;
    private AbstractBookingManager bc;
    private Sport padel;
    private UserClub userClub;
    private Club gracciano;
    private User mark;
    private User gigi;
    private User eli;
    private User pippo;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.bm = new BookingManager(monitor);
        this.bc = new BookingChecker(bm);

        this.padel = new Padel();

        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        this.mark = marco.subscribe(bc, "mark");
        Person luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        this.gigi = luigi.subscribe(bc, "gigi");
        Person filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
        this.pippo = filippo.subscribe(bc, "pippo");
        Person elisa = new Person("elisa", "landi", "elisalandi@mail.it");
        this.eli = elisa.subscribe(bc, "eli");

        this.gracciano = new Club("Gracciano", 9,18,5);
        gracciano.addField("Padel 1", padel,15);
        this.userClub = gracciano.subscribe(bc, 100);
    }

    @Test
    void createUser() {
        Person francesca = new Person("francesc","bianchi","francebi@mail.it");
        User france = bm.createUser(francesca, "france", bc);
        assertEquals(france, bm.bd.getUser(4));
    }

    @Test
    void removeUser() {
        assertEquals(mark, bm.bd.getUser(0));
        bm.removeUser(mark);
        assertFalse(bm.bd.containsUser(mark));
    }

    @Test
    void getUsersSize() {
        assertEquals(4, bm.getUsersSize());
    }

    @Test
    void topUpUserBalance() {
        bm.topUpUserBalance(mark, 100);
        assertEquals(100, mark.getBalance());
    }

    @Test
    void getBooking() {
        bm.topUpUserBalance(mark,100);
        bm.topUpUserBalance(gigi,100);
        bm.topUpUserBalance(pippo,100);
        bm.topUpUserBalance(eli,100);

        mark.bookField(padel, "Gracciano", "12/05/2022", 10, "gigi", "eli", "pippo");
        assertEquals(bm.bd.getBooking(1), bm.getBooking(1));
    }


    @Test
    void addClub() {
        Club club = new Club("Firenze",9,16,8);
        UserClub uc = bm.addClub(club,100);
        assertEquals(bm.bd.getClub(1), uc);
    }

    @Test
    void getClubsSize() {
        assertEquals(bm.bd.getClubsSize(), bm.getClubsSize());
    }

    @Test
    void addClubMember() {
        bm.topUpUserBalance(mark,150);
        bm.addClubMember(mark,userClub);
        assertTrue(userClub.isMember(mark));
    }

    @Test
    void rechargeAccount() {
        bm.rechargeAccount(mark,100);
        assertEquals(100, mark.getBalance());
    }

    @Test
    void addUserFavouriteClub() {
        bm.addUserFavouriteClub(mark, userClub);
        assertTrue(mark.getFavouriteClubs().contains(userClub));
    }

    @Test
    void addResult() {
        ArrayList<User> players = new ArrayList<>();
        players.add(mark);
        players.add(eli);
        players.add(gigi);
        players.add(pippo);

        ArrayList<String> winners = new ArrayList<>();
        winners.add("mark");
        winners.add("eli");
        bm.addResult(padel,players,winners);

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
    void releaseField() {
        ArrayList<User> players = new ArrayList<>();
        players.add(mark);
        players.add(eli);
        players.add(gigi);
        players.add(pippo);

        bm.topUpUserBalance(mark,100);
        bm.topUpUserBalance(gigi,100);
        bm.topUpUserBalance(pippo,100);
        bm.topUpUserBalance(eli,100);

        bm.createBooking(userClub, gracciano.fields.get(0), LocalDate.parse("17/05/2022",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15,players);
        bm.releaseField(1);

        assertTrue(gracciano.fields.get(0).timeTable.get(LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") )).contains(15));
    }

    @Test
    void payJoinClub() {
        bm.topUpUserBalance(mark,200);
        bm.payJoinClub(mark,userClub);
    }

    @Test
    void addBookingPlayer() {
        bm.topUpUserBalance(mark,100);
        bm.topUpUserBalance(eli,100);
        bm.createBlindBooking(userClub, gracciano.fields.get(0), LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15,mark);
        bm.addBookingPlayer(eli, 1);
        assertEquals(2,bm.bd.getBooking(1).getPlayers().size());
    }

    @Test
    void releaseSpot() {
        bm.topUpUserBalance(mark,100);
        bm.topUpUserBalance(eli,100);
        bm.createBlindBooking(userClub, gracciano.fields.get(0), LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15,mark);
        bm.addBookingPlayer(eli,1);
        assertEquals(85,mark.getBalance());

        bm.releaseSpot(mark, 1);
        assertFalse(bm.bd.getBooking(1).containsUser(mark));
        assertEquals(100, mark.getBalance());

        bm.releaseSpot(eli,1);
        assertEquals(0, bm.bd.getBookingsSize());
    }

    @Test
    void createBooking() {
        ArrayList<User> players = new ArrayList<>();
        players.add(mark);
        players.add(eli);
        players.add(gigi);
        players.add(pippo);

        bm.topUpUserBalance(mark,100);
        bm.topUpUserBalance(gigi,100);
        bm.topUpUserBalance(pippo,100);
        bm.topUpUserBalance(eli,100);

        bm.createBooking(userClub, gracciano.fields.get(0), LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15,players);

        assertEquals(85,mark.getBalance());
        assertEquals(85,gigi.getBalance());
        assertEquals(85,eli.getBalance());
        assertEquals(85,pippo.getBalance());

        assertEquals(1, bm.bd.getBookingsSize());
    }

    @Test
    void createBlindBooking() {
        bm.topUpUserBalance(mark,100);

        bm.createBlindBooking(userClub, gracciano.fields.get(0), LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15,mark);

        assertEquals(85,mark.getBalance());
        assertEquals(1, bm.bd.getBooking(1).getPlayers().size());

        assertTrue(bm.bd.getBooking(1) instanceof BlindBooking);
    }

    @Test
    void pay(){
        bm.topUpUserBalance(mark, 100);
        bm.pay(mark, userClub, gracciano.fields.get(0));

        assertEquals(85,mark.getBalance());
    }

    @Test
    void holdField(){
        bm.holdField(gracciano.fields.get(0), LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy") ),15);

        assertFalse(gracciano.fields.get(0).timeTable.get(LocalDate.parse("17/05/2022",
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))).contains(15));
    }

    @Test
    void refund(){
        bm.refund(mark, userClub, gracciano.fields.get(0));
        assertEquals(15, mark.getBalance());
    }

    @Test
    void getUser() {
        assertEquals(mark, bm.getUser(0));
    }

    @Test
    void getClub() {
        assertEquals(userClub, bm.getClub(0));
    }
}