package BusinessLogic;

import DAO.*;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RequestManagerTest {
    private static RequestManager rm;
    private static UserDao userDao;
    private static ClubDao clubDao;
    private static BookingDao bookingDao;
    private static User user4, user5, user6, user7;
    private static UserClub club2;

    @BeforeAll
    static void setUp() {
        bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        clubDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        userDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        rm = RequestManager.getInstance();
        Person person1 = new Person("Lorenzo","Rossi","lor.rossi@gmail.com");
        Person person2 = new Person("Elisabetta","Bianchi","bett.bianchi@gmail.com");
        Person person3 = new Person("Martina","Gialli","mart.gialli@gmail.com");
        Person person4 = new Person("Camilla","Verdi","cam.verdi@gmail.com");
        user4 = person1.subscribe(rm, "lory");
        user5 = person2.subscribe(rm, "ely");
        user6 = person3.subscribe(rm, "martuz");
        user7 = person4.subscribe(rm, "camy");
        Club clb2 = new Club("Firenze", 9, 23,5);
        clb2.addField("Padel 1", Sport.PADEL, 5);
        club2 = clb2.subscribe(rm, 100);
    }

    @Test
    void requestBooking() {
        ArrayList<String> players = new ArrayList<>();
        players.add("lory");
        players.add("ely");
        players.add("martuz");
        players.add("camy");
        user4.setBalance(100);
        user5.setBalance(100);
        user6.setBalance(100);
        user7.setBalance(100);

        int exp = bookingDao.getBookingsSize() + 1;
        rm.requestBooking(Sport.PADEL, "Firenze", "02/07/2022", 10, players);
        assertEquals(exp, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user7));
    }

    @Test
    void addUser() {
        Person mike = new Person("mike","Rossi","mike.rossi@gmail.com");
        User miky = rm.addUser(mike, "miky");
        assertTrue(userDao.containsUser(miky));
    }

    @Test
    void addClub() {
        Club clb3 = new Club("Colle", 30, 10,5);
        UserClub club3 = clb3.subscribe(rm, 100);
        assertTrue(clubDao.containsClub(club3));
    }

    @Test
    void topUpBalance() {
        user4.setBalance(0);
        rm.topUpBalance(user4, 10);
        assertEquals(10, user4.getBalance());
    }

    @Test
    void requestBlindBooking() {
        int exp = bookingDao.getBookingsSize() + 1;
        user4.setBalance(100);
        rm.requestBlindBooking(Sport.PADEL, "Firenze", "02/07/2022", 12, user4);
        assertEquals(exp, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().
                toList().get(bookingDao.getKeySet().size()-1)).containsUser(user4));
    }

    @Test
    void requestSpot() {
        user4.setBalance(100);
        user5.setBalance(100);
        user6.setBalance(100);
        user7.setBalance(100);
        rm.requestBlindBooking(Sport.PADEL, "Firenze", "02/07/2022", 10, user4);
        rm.requestSpot(user5, bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        rm.requestSpot(user6, bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        rm.requestSpot(user7, bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user5));
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user6));
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user7));
    }

    @Test
    void requestJoinClub() {
        user4.setBalance(1000);
        rm.requestJoinClub(user4, "Firenze");
        assertTrue(club2.isMember(user4));
    }

    @Test
    void deleteUserBooking() {
        user4.setBalance(100);
        user5.setBalance(100);
        user6.setBalance(100);
        user7.setBalance(100);
        ArrayList<String> players = new ArrayList<>();
        players.add("lory");
        players.add("ely");
        players.add("martuz");
        players.add("camy");

        rm.requestBooking(Sport.PADEL, "Firenze", "02/08/2022", 10, players);

        rm.requestBlindBooking(Sport.PADEL, "Firenze", "02/09/2022",
                12, user4);
        rm.requestSpot(user5, bookingDao.getKeySet().stream().
                toList().get(bookingDao.getKeySet().size()-1));

        int exp = bookingDao.getBookingsSize() - 1;
        rm.deleteUserBooking(user5, bookingDao.getKeySet().stream().
                toList().get(bookingDao.getKeySet().size()-1));
        rm.deleteUserBooking(user4, bookingDao.getKeySet().stream().
                toList().get(bookingDao.getKeySet().size()-2));

        assertEquals(exp, bookingDao.getBookingsSize());
        assertFalse(bookingDao.getBooking(bookingDao.getKeySet().stream().
                toList().get(bookingDao.getKeySet().size()-1)).containsUser(user5));

        rm.deleteUserBooking(user4, bookingDao.getKeySet().stream().
                toList().get(bookingDao.getKeySet().size()-1));
        assertEquals(--exp, bookingDao.getBookingsSize());
    }

    @Test
    void addMatchResult() {
        user4.setBalance(100);
        user5.setBalance(100);
        user6.setBalance(100);
        user7.setBalance(100);
        ArrayList<String> players = new ArrayList<>();
        players.add("lory");
        players.add("ely");
        players.add("martuz");
        players.add("camy");
        rm.requestBooking(Sport.PADEL, "Firenze", "02/07/2022", 15, players);
        ArrayList<String> winners = new ArrayList<>();
        winners.add("lory");
        winners.add("ely");
        rm.addMatchResult(winners, bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        assertEquals(0, user4.record.get(Sport.PADEL)[0]);
        assertEquals(1, user4.record.get(Sport.PADEL)[1]);
        assertEquals(0, user5.record.get(Sport.PADEL)[0]);
        assertEquals(1, user5.record.get(Sport.PADEL)[1]);
        assertEquals(1, user6.record.get(Sport.PADEL)[0]);
        assertEquals(0, user6.record.get(Sport.PADEL)[1]);
        assertEquals(1, user7.record.get(Sport.PADEL)[0]);
        assertEquals(0, user7.record.get(Sport.PADEL)[1]);
    }

    @Test
    void deleteUser() {
        Person ludovica = new Person("Ludovica","Rossi"
                ,"ludy.rossi@gmail.com");
        User ludy = rm.addUser(ludovica, "ludy");
        rm.deleteUser(ludy);
        assertFalse(userDao.containsUser(ludy));
    }

    @Test
    void addFavouriteClub() {
        rm.addFavouriteClub(user4, "Firenze");
        assertEquals(club2, user4.getFavouriteClubs().get(0));
    }
}