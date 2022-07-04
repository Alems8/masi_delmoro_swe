package BusinessLogic;

import DAO.BookingDao;
import DAO.ClubDao;
import DAO.DaoFactory;
import DAO.UserDao;
import DomainModel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ClubControllerTest {
    RequestManager rm;
    UserDao userDao;
    ClubDao clubDao;
    BookingDao bookingDao;
    User user1, user2, user3, user4;
    UserClub club1;
    ClubController cc;


    @BeforeEach
    void setUp() {
        this.bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        this.clubDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        this.userDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        RequestManager rm = new RequestManager();
        this.rm = rm;
        this.cc = rm.cc;
        Person lorenzo = new Person("Lorenzo","Rossi","lore.rossi@gmail.com");
        Person elisabetta = new Person("Elisabetta","Bianchi","betti.bianchi@gmail.com");
        Person martina = new Person("Martina","Gialli","marti.gialli@gmail.com");
        Person camilla = new Person("Camilla","Verdi","cami.verdi@gmail.com");
        this.user1 = lorenzo.subscribe(rm, "lore");
        this.user2 = elisabetta.subscribe(rm, "eli");
        this.user3 = martina.subscribe(rm, "marti");
        this.user4 = camilla.subscribe(rm, "cami");
        user1.addFunds(110);
        user3.addFunds(100);
        user4.addFunds(100);
        Club clb1 = new Club("LaFiorita", 9, 23,5);
        clb1.addField("Padel 1", Sport.PADEL, 5);
        this.club1 = clb1.subscribe(rm, 100);
    }


    @Test
    void setCurrentDate() {
        LocalDate date = LocalDate.now();
        cc.setCurrentDate(date);
        assertEquals(date, cc.getCurrentDate());
    }


    @Test
    void setCurrentHour() {
        cc.setCurrentHour(15);
        assertEquals(15,cc.getCurrentHour());
    }

    @Test
    void setCurrentClub() {
        cc.setCurrentClub(club1);
        assertEquals(club1, cc.getCurrentClub());
    }

    @Test
    void testSetCurrentClub() throws WrongNameException {
        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> cc.setCurrentClub("UPP")
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));

        cc.setCurrentClub("LaFiorita");
        assertEquals(club1, cc.getCurrentClub());
    }


    @Test
    void setCurrentField() {
        cc.setCurrentField(club1.getClub().fields.get(0));
        assertEquals(club1.getClub().fields.get(0), cc.getCurrentField());
    }


    @Test
    void findField() throws NoFreeFieldException {
        Sport sport = Sport.PADEL;
        LocalDate date = LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        cc.setCurrentClub(club1);

        NoFreeFieldException thrown = assertThrows(
                NoFreeFieldException.class,
                () -> cc.findField(sport, date,5)
        );
        assertTrue(thrown.getMessage().contains("Non ci sono campi disponibili"));

        cc.findField(sport, date, 15);
        assertEquals(club1.getClub().fields.get(0), cc.getCurrentField());
        assertEquals(date, cc.getCurrentDate());
        assertEquals(15, cc.getCurrentHour());

    }

    @Test
    void holdField() {
        cc.setCurrentField(club1.getClub().fields.get(0));
        cc.setCurrentHour(15);
        LocalDate date = LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        cc.setCurrentDate(date);

        cc.holdField();
        assertFalse(club1.getClub().fields.get(0).timeTable.get(date).contains(15));
    }

    @Test
    void releaseField() {
        cc.setCurrentField(club1.getClub().fields.get(0));
        cc.setCurrentHour(15);
        LocalDate date = LocalDate.parse("04/07/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        cc.setCurrentDate(date);

        cc.releaseField();
        assertTrue(club1.getClub().fields.get(0).timeTable.get(date).contains(15));
    }

    @Test
    void addClub() {
        Club club2 = new Club("Marte",10,20,4);
        UserClub uc2 = club2.subscribe(rm, 100);
        assertEquals(2,clubDao.getClubsSize());
        assertTrue(clubDao.containsClub(uc2));
    }

    @Test
    void addClubMember() throws AlreadySubscribedException {
        cc.setCurrentClub(club1);
        cc.addClubMember(user1);
        assertTrue(club1.isMember(user1));

        AlreadySubscribedException thrown = assertThrows(
                AlreadySubscribedException.class,
                () -> cc.addClubMember(user1)
        );
        assertTrue(thrown.getMessage().contains("Utente già registrato"));
    }
}