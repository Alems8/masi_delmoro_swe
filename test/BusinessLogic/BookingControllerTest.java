package BusinessLogic;

import DAO.BookingDao;
import DAO.ClubDao;
import DAO.DaoFactory;
import DAO.UserDao;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BookingControllerTest {
    private static RequestManager rm;
    private static BookingDao bookingDao;
    private static User user1, user2, user3, user4, user5;
    private static UserClub club1;
    private static BookingController bc;
    private static UserController uc;
    private static ClubController cc;


    @BeforeAll
    static void setUp() {
        bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        rm = RequestManager.getInstance();
        bc = rm.bc;
        cc = rm.cc;
        uc = rm.uc;
        Person lorenzo = new Person("Lorenzo","Rossi","lore.rossi@gmail.com");
        Person elisabetta = new Person("Elisabetta","Bianchi","betti.bianchi@gmail.com");
        Person martina = new Person("Martina","Gialli","marti.gialli@gmail.com");
        Person camilla = new Person("Camilla","Verdi","cami.verdi@gmail.com");
        Person camillo = new Person("Camillo","Verdi","camo.verdi@gmail.com");
        user1 = lorenzo.subscribe(rm, "lore");
        user2 = elisabetta.subscribe(rm, "eli");
        user3 = martina.subscribe(rm, "marti");
        user4 = camilla.subscribe(rm, "cami");
        user5 = camillo.subscribe(rm, "camo");

        Club clb1 = new Club("LaFiorita", 9, 23,5);
        clb1.addField("Padel 1", Sport.PADEL, 5);
        club1 = clb1.subscribe(rm, 100);
    }

    @Test
    void setCurrentBooking() throws WrongKeyException {
        user1.setBalance(110);

        WrongKeyException thrown = assertThrows(
                WrongKeyException.class,
                () -> bc.setCurrentBooking(1)
        );
        assertTrue(thrown.getMessage().contains("La chiave della prenotazione è sbagliata"));

        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking);

        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        assertEquals(booking, bc.getCurrentBooking());

    }

    @Test
    void createBooking() throws WrongNameException, NoFreeSpotException {
        cc.setCurrentClub(club1);
        cc.setCurrentField(club1.getClub().fields.get(0));
        cc.setCurrentDate(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cc.setCurrentHour(15);
        user1.setBalance(110);
        uc.setCurrentPlayers(new ArrayList<>(){{add("lore");}}, 1);

        club1.getClub().fields.get(0).timeTable.put(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),new ArrayList<>(){{add(15);}});

        int exp = bookingDao.getBookingsSize();
        bc.createBooking();
        assertEquals(++exp, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user1));
    }

    @Test
    void createBlindBooking() {
        cc.setCurrentClub(club1);
        cc.setCurrentField(club1.getClub().fields.get(0));
        cc.setCurrentDate(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cc.setCurrentHour(15);
        user1.setBalance(110);
        uc.setCurrentUser(user1);

        club1.getClub().fields.get(0).timeTable.put(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),new ArrayList<>(){{add(15);}});
        int exp = bookingDao.getBookingsSize();
        bc.createBlindBooking();
        assertEquals(++exp, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user1));
    }

    @Test
    void checkBlindBooking() throws WrongKeyException {
        user1.setBalance(110);
        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking);
        ((BlindBooking)booking).addPlayer(user2);
        ((BlindBooking)booking).addPlayer(user3);
        ((BlindBooking)booking).addPlayer(user4);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));


        NoFreeSpotException thrown = assertThrows(
                NoFreeSpotException.class,
                () -> bc.checkBlindBooking()
        );
        assertTrue(thrown.getMessage().contains("Nessun posto disponibile"));

        Booking booking1 = new PrivateBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, new ArrayList<>(){{add(user1);}});
        bookingDao.addBooking(booking1);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        WrongKeyException thrown2 = assertThrows(
                WrongKeyException.class,
                () -> bc.checkBlindBooking()
        );
        assertTrue(thrown2.getMessage().contains("La chiave della prenotazione è sbagliata"));
    }

    @Test
    void addBookingPlayer() throws WrongKeyException {
        user1.setBalance(110);
        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user3);

        user3.setBalance(100);
        bc.addBookingPlayer();
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user3));
    }

    @Test
    void removeBookingPlayer() throws WrongKeyException {
        user1.setBalance(110);
        user2.setBalance(100);
        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking);
        ((BlindBooking)booking).addPlayer(user2);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user2);

        bc.removeBookingPlayer();
        assertFalse(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user2));
    }

    @Test
    void deleteBooking() throws WrongKeyException {
        user1.setBalance(110);
        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        int exp = bookingDao.getBookingsSize();
        assertEquals(exp,bookingDao.getBookingsSize());
        bc.deleteBooking();
        assertEquals(--exp,bookingDao.getBookingsSize());
    }

    @Test
    void deleteUserBooking() throws WrongKeyException {
        user1.setBalance(110);
        user2.setBalance(100);
        user3.setBalance(100);

        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user2);
        WrongKeyException thrown1 = assertThrows(
                WrongKeyException.class,
                () -> bc.deleteUserBooking()
        );
        assertTrue(thrown1.getMessage().contains("La chiave della prenotazione è sbagliata"));

        int exp = bookingDao.getBookingsSize();
        uc.setCurrentUser(user1);
        bc.deleteUserBooking();
        assertEquals(115,uc.getCurrentUser().getBalance());
        assertEquals(--exp,bookingDao.getBookingsSize());

        Booking booking2 = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user1);
        bookingDao.addBooking(booking2);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        ((BlindBooking)booking2).addPlayer(user2);
        bc.deleteUserBooking();
        assertEquals(120,uc.getCurrentUser().getBalance());
        assertEquals(++exp,bookingDao.getBookingsSize());

        Booking booking3 = new PrivateBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, new ArrayList<>(){{add(user1);add(user3);}});
        bookingDao.addBooking(booking3);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        assertEquals(++exp, bookingDao.getBookingsSize());
        bc.deleteUserBooking();
        assertEquals(105, user3.getBalance());
        assertEquals(--exp, bookingDao.getBookingsSize());
    }

    @Test
    void addMatchResult() throws WrongKeyException, WrongNameException, NoFreeSpotException {
        Booking booking = new PrivateBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, new ArrayList<>(){{add(user1);add(user3);}});
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(1);
        uc.setCurrentPlayers(new ArrayList<>(){{add("lore");}},1);

        bc.addMatchResult();
        assertEquals(0, user3.record.get(Sport.PADEL)[1]);
        assertEquals(1, user3.record.get(Sport.PADEL)[0]);
        assertEquals(0, user1.record.get(Sport.PADEL)[0]);
        assertEquals(1, user1.record.get(Sport.PADEL)[1]);
        assertEquals(0, bookingDao.getBookingsSize());
    }

    @Test
    void checkActiveBookings() throws WrongKeyException, PendingBookingException {
        user5.setBalance(110);
        Booking booking = new BlindBooking(club1, club1.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user5);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user5);

        PendingBookingException thrown = assertThrows(
                PendingBookingException.class,
                () -> bc.checkActiveBookings()
        );
        assertTrue(thrown.getMessage().contains("Hai delle prenotazioni in sospeso"));

        int exp = bookingDao.getBookingsSize();
        bc.deleteBooking();
        bc.checkActiveBookings();
        assertEquals(--exp,bookingDao.getBookingsSize());
    }
}