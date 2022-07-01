package BusinessLogic;

import DAO.*;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RequestManagerTest {
    RequestManager rm;
    UserDao userDao;
    ClubDao clubDao;
    BookingDao bookingDao;
    User user1, user2, user3, user4;
    UserClub club1;

    @BeforeEach
    void setUp() {
        this.bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        this.clubDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        this.userDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        this.rm = new RequestManager();
        Person lorenzo = new Person("Lorenzo","Rossi","lore.rossi@gmail.com");
        Person elisabetta = new Person("Elisabetta","Bianchi","betti.bianchi@gmail.com");
        Person martina = new Person("Martina","Gialli","marti.gialli@gmail.com");
        Person camilla = new Person("Camilla","Verdi","cami.verdi@gmail.com");
        this.user1 = lorenzo.subscribe(rm, "lore");
        this.user2 = elisabetta.subscribe(rm, "eli");
        this.user3 = martina.subscribe(rm, "marti");
        this.user4 = camilla.subscribe(rm, "cami");
        user1.addFunds(100);
        user2.addFunds(100);
        user3.addFunds(100);
        user4.addFunds(100);
        Club clb1 = new Club("LaFiorita", 9, 23,5);
        clb1.addField("Padel 1", Sport.PADEL, 5);
        this.club1 = clb1.subscribe(rm, 100);
    }

    @Test
    void requestBooking() {
        ArrayList<String> players = new ArrayList<>();
        players.add("lore");
        players.add("eli");
        players.add("marti");
        players.add("cami");
        rm.requestBooking(Sport.PADEL, "LaFiorita", "02/07/2022", 10, players);
        assertEquals(1, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(1).containsUser(user4));
    }

    @Test
    void addUser() {
        Person ludovico = new Person("Ludovico","Rossi","lud.rossi@gmail.com");
        User ludo = rm.addUser(ludovico, "ludo");
        assertTrue(userDao.containsUser(ludo));
    }

    @Test
    void addClub() {
        Club clb2 = new Club("Gracciano", 30, 10,5);
        UserClub club2 = clb2.subscribe(rm, 100);
        assertTrue(clubDao.containsClub(club2));
    }

    @Test
    void topUpBalance() {
        Person ludovico = new Person("Ludovico","Rossi","lud.rossi@gmail.com");
        User ludo = rm.addUser(ludovico, "ludo");
        rm.topUpBalance(ludo, 10);
        assertEquals(10, ludo.getBalance());
    }

    @Test
    void requestBlindBooking() {
        rm.requestBlindBooking(Sport.PADEL, "LaFiorita", "02/07/2022", 10, user1);
        assertEquals(1, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(1).containsUser(user1));
    }

    @Test
    void requestSpot() {
        rm.requestBlindBooking(Sport.PADEL, "LaFiorita", "02/07/2022", 10, user1);
        rm.requestSpot(user2, 1);
        rm.requestSpot(user3, 1);
        rm.requestSpot(user4, 1);
        assertTrue(bookingDao.getBooking(1).containsUser(user2));
        assertTrue(bookingDao.getBooking(1).containsUser(user3));
        assertTrue(bookingDao.getBooking(1).containsUser(user4));
    }

    @Test
    void requestJoinClub() {
        rm.requestJoinClub(user1, "LaFiorita");
        assertTrue(club1.isMember(user1));
    }

    @Test
    void deleteUserBooking() {
        ArrayList<String> players = new ArrayList<>();
        players.add("lore");
        players.add("eli");
        players.add("marti");
        players.add("cami");
        rm.requestBooking(Sport.PADEL, "LaFiorita", "02/07/2022", 10, players);

        rm.requestBlindBooking(Sport.PADEL, "LaFiorita", "02/07/2022", 12, user1);
        rm.requestSpot(user2, 2);

        rm.deleteUserBooking(user2, 2);
        rm.deleteUserBooking(user1, 1);
        assertEquals(1, bookingDao.getBookingsSize());
        assertFalse(bookingDao.getBooking(2).containsUser(user2));

        rm.deleteUserBooking(user1, 2);
        assertEquals(0, bookingDao.getBookingsSize());
    }

    @Test
    void addMatchResult() {
        ArrayList<String> players = new ArrayList<>();
        players.add("lore");
        players.add("eli");
        players.add("marti");
        players.add("cami");
        rm.requestBooking(Sport.PADEL, "LaFiorita", "02/07/2022", 10, players);
        ArrayList<String> winners = new ArrayList<>();
        winners.add("lore");
        winners.add("eli");
        rm.addMatchResult(winners, 1);
        assertEquals(0, user1.record.get(Sport.PADEL)[0]);
        assertEquals(1, user1.record.get(Sport.PADEL)[1]);
        assertEquals(0, user2.record.get(Sport.PADEL)[0]);
        assertEquals(1, user2.record.get(Sport.PADEL)[1]);
        assertEquals(1, user3.record.get(Sport.PADEL)[0]);
        assertEquals(0, user3.record.get(Sport.PADEL)[1]);
        assertEquals(1, user4.record.get(Sport.PADEL)[0]);
        assertEquals(0, user4.record.get(Sport.PADEL)[1]);
    }

    @Test
    void deleteUser() {
        rm.deleteUser(user1);
        assertFalse(userDao.containsUser(user1));
    }

    @Test
    void addFavouriteClub() {
        rm.addFavouriteClub(user1, "LaFiorita");
        assertEquals(club1, user1.getFavouriteClubs().get(0));
    }
}