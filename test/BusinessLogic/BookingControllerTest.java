package BusinessLogic;

import DAO.BookingDao;
import DAO.DaoFactory;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BookingControllerTest {
    private static RequestManager rm;
    private static BookingDao bookingDao;
    private static User user8, user9, user10, user11, user12;
    private static UserClub club4;
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
        Person person5 = new Person("Lorenzo","Rossi","lore1.rossi@gmail.com");
        Person person6 = new Person("Elisabetta","Bianchi","betti1.bianchi@gmail.com");
        Person person7 = new Person("Martina","Gialli","marti1.gialli@gmail.com");
        Person person8 = new Person("Camilla","Verdi","cami1.verdi@gmail.com");
        Person person9 = new Person("Camillo","Verdi","camo1.verdi@gmail.com");
        user8 = person5.subscribe(rm, "loren");
        user9 = person6.subscribe(rm, "elis");
        user10 = person7.subscribe(rm, "martin");
        user11 = person8.subscribe(rm, "camil");
        user12 = person9.subscribe(rm, "camol");

        Club clb4 = new Club("Poggibonsi", 9, 23,5);
        clb4.addField("Padel 1", Sport.PADEL, 5);
        club4 = clb4.subscribe(rm, 100);
    }

    @Test
    void setCurrentBooking() throws WrongKeyException {
        user8.setBalance(110);

        WrongKeyException thrown = assertThrows(
                WrongKeyException.class,
                () -> bc.setCurrentBooking(100)
        );
        assertTrue(thrown.getMessage().contains("La chiave della prenotazione è sbagliata"));

        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user8);
        bookingDao.addBooking(booking);

        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1));
        assertEquals(booking, bc.getCurrentBooking());

    }

    @Test
    void createBooking() throws WrongNameException, NoFreeSpotException {
        cc.setCurrentClub(club4);
        cc.setCurrentField(club4.getClub().fields.get(0));
        cc.setCurrentDate(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cc.setCurrentHour(15);
        user8.setBalance(110);
        uc.setCurrentPlayers(new ArrayList<>(){{add("loren");}}, 1);

        club4.getClub().fields.get(0).timeTable.put(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),new ArrayList<>(){{add(15);}});

        int exp = bookingDao.getBookingsSize();
        bc.createBooking();
        assertEquals(++exp, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user8));
    }

    @Test
    void createBlindBooking() {
        cc.setCurrentClub(club4);
        cc.setCurrentField(club4.getClub().fields.get(0));
        cc.setCurrentDate(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cc.setCurrentHour(15);
        user8.setBalance(110);
        uc.setCurrentUser(user8);

        club4.getClub().fields.get(0).timeTable.put(LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),new ArrayList<>(){{add(15);}});
        int exp = bookingDao.getBookingsSize();
        bc.createBlindBooking();
        assertEquals(++exp, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user8));
    }

    @Test
    void checkBlindBooking() throws WrongKeyException {
        user8.setBalance(110);
        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user8);
        bookingDao.addBooking(booking);
        ((BlindBooking)booking).addPlayer(user9);
        ((BlindBooking)booking).addPlayer(user10);
        ((BlindBooking)booking).addPlayer(user11);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1));


        NoFreeSpotException thrown = assertThrows(
                NoFreeSpotException.class,
                () -> bc.checkBlindBooking()
        );
        assertTrue(thrown.getMessage().contains("Nessun posto disponibile"));

        Booking booking1 = new PrivateBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, new ArrayList<>(){{add(user8);}});
        bookingDao.addBooking(booking1);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1));
        WrongKeyException thrown2 = assertThrows(
                WrongKeyException.class,
                () -> bc.checkBlindBooking()
        );
        assertTrue(thrown2.getMessage().contains("La chiave della prenotazione è sbagliata"));
    }

    @Test
    void addBookingPlayer() throws WrongKeyException {
        user8.setBalance(110);
        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user8);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user10);

        user10.setBalance(100);
        bc.addBookingPlayer();
        assertTrue(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user10));
    }

    @Test
    void removeBookingPlayer() throws WrongKeyException {
        user8.setBalance(110);
        user9.setBalance(100);
        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user8);
        bookingDao.addBooking(booking);
        ((BlindBooking)booking).addPlayer(user9);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user9);

        bc.removeBookingPlayer();
        assertFalse(bookingDao.getBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1)).containsUser(user9));
    }

    @Test
    void deleteBooking() throws WrongKeyException {
        user8.setBalance(110);
        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user8);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        int exp = bookingDao.getBookingsSize();
        assertEquals(exp,bookingDao.getBookingsSize());
        bc.deleteBooking();
        assertEquals(--exp,bookingDao.getBookingsSize());
    }

    @Test
    void deleteUserBooking() throws WrongKeyException {
        user8.setBalance(110);
        user9.setBalance(100);
        user10.setBalance(100);

        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.now(),15, user8);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user9);
        WrongKeyException thrown1 = assertThrows(
                WrongKeyException.class,
                () -> bc.deleteUserBooking()
        );
        assertTrue(thrown1.getMessage().contains("La chiave della prenotazione è sbagliata"));

        int exp = bookingDao.getBookingsSize();
        uc.setCurrentUser(user8);
        bc.deleteUserBooking();
        assertEquals(115,uc.getCurrentUser().getBalance());
        assertEquals(--exp,bookingDao.getBookingsSize());

        Booking booking2 = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.now(),15, user8);
        bookingDao.addBooking(booking2);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1));
        ((BlindBooking)booking2).addPlayer(user9);
        bc.deleteUserBooking();
        assertEquals(120,uc.getCurrentUser().getBalance());
        assertEquals(++exp,bookingDao.getBookingsSize());

        Booking booking3 = new PrivateBooking(club4, club4.getClub().fields.get(0), LocalDate.now(),15, new ArrayList<>(){{add(user8);add(user10);}});
        bookingDao.addBooking(booking3);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().stream().sorted().toList().get(bookingDao.getKeySet().size()-1));
        assertEquals(++exp, bookingDao.getBookingsSize());
        bc.deleteUserBooking();
        assertEquals(105, user10.getBalance());
        assertEquals(--exp, bookingDao.getBookingsSize());
    }

    @Test
    void addMatchResult() throws WrongKeyException, WrongNameException, NoFreeSpotException {
        Booking booking = new PrivateBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, new ArrayList<>(){{add(user8);add(user10);}});
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentPlayers(new ArrayList<>(){{add("loren");}},1);

        int exp = bookingDao.getBookingsSize()-1;
        bc.addMatchResult();
        assertEquals(0, user10.record.get(Sport.PADEL)[1]);
        assertEquals(1, user10.record.get(Sport.PADEL)[0]);
        assertEquals(0, user8.record.get(Sport.PADEL)[0]);
        assertEquals(1, user8.record.get(Sport.PADEL)[1]);
        assertEquals(exp, bookingDao.getBookingsSize());
    }

    @Test
    void checkActiveBookings() throws WrongKeyException, PendingBookingException {
        user12.setBalance(110);
        Booking booking = new BlindBooking(club4, club4.getClub().fields.get(0), LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")),15, user12);
        bookingDao.addBooking(booking);
        bc.setCurrentBooking(bookingDao.getKeySet().stream().toList().get(bookingDao.getKeySet().size()-1));
        uc.setCurrentUser(user12);

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