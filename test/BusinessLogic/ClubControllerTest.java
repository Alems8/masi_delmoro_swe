package BusinessLogic;

import DAO.ClubDao;
import DAO.DaoFactory;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ClubControllerTest {
    private static RequestManager rm;
    private static ClubDao clubDao;
    private static User user13;
    private static UserClub club6;
    private static ClubController cc;


    @BeforeAll
    static void setUp() {
        clubDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        rm = RequestManager.getInstance();
        cc = rm.cc;
        Person person10 = new Person("Lorenzo","Rossi","lore.ross@gmail.com");
        user13 = person10.subscribe(rm, "lorenz");
        Club clb6 = new Club("Novoli", 9, 23,5);
        clb6.addField("Padel 1", Sport.PADEL, 5);
        club6 = clb6.subscribe(rm, 100);
    }


    @Test
    void setCurrentDate() {
        LocalDate date = LocalDate.now();
        cc.setCurrentDate(date);
        assertEquals(date, cc.getCurrentDate());
    }


    @Test
    void setCurrentHour() {
        cc.setCurrentHour(14);
        assertEquals(14,cc.getCurrentHour());
    }

    @Test
    void setCurrentClub() {
        cc.setCurrentClub(club6);
        assertEquals(club6, cc.getCurrentClub());
    }

    @Test
    void testSetCurrentClub() throws WrongNameException {
        WrongNameException thrown = assertThrows(
                WrongNameException.class,
                () -> cc.setCurrentClub("Empoli")
        );
        assertTrue(thrown.getMessage().contains("Il nome inserito non è coretto"));

        cc.setCurrentClub("Novoli");
        assertEquals(club6, cc.getCurrentClub());
    }


    @Test
    void setCurrentField() {
        cc.setCurrentField(club6.getClub().fields.get(0));
        assertEquals(club6.getClub().fields.get(0), cc.getCurrentField());
    }


    @Test
    void findField() throws NoFreeFieldException {
        Sport sport = Sport.PADEL;
        LocalDate date = LocalDate.now();
        cc.setCurrentClub(club6);

        NoFreeFieldException thrown = assertThrows(
                NoFreeFieldException.class,
                () -> cc.findField(sport, date,5)
        );
        assertTrue(thrown.getMessage().contains("Non ci sono campi disponibili"));

        cc.findField(sport, date, 16);
        assertEquals(club6.getClub().fields.get(0), cc.getCurrentField());
        assertEquals(date, cc.getCurrentDate());
        assertEquals(16, cc.getCurrentHour());

    }

    @Test
    void holdField() {
        cc.setCurrentField(club6.getClub().fields.get(0));
        cc.setCurrentHour(15);
        LocalDate date = LocalDate.now();
        cc.setCurrentDate(date);

        cc.holdField();
        assertFalse(club6.getClub().fields.get(0).timeTable.get(date).contains(15));
    }

    @Test
    void releaseField() {
        cc.setCurrentField(club6.getClub().fields.get(0));
        cc.setCurrentHour(13);
        LocalDate date = LocalDate.now();
        cc.setCurrentDate(date);
        cc.holdField();

        cc.releaseField();
        assertTrue(club6.getClub().fields.get(0).timeTable.get(date).contains(13));
    }

    @Test
    void addClub() {
        int exp = clubDao.getClubsSize() + 1;
        Club club2 = new Club("Marte",10,20,4);
        UserClub uc2 = club2.subscribe(rm, 100);
        assertEquals(exp,clubDao.getClubsSize());
        assertTrue(clubDao.containsClub(uc2));
    }

    @Test
    void addClubMember() throws AlreadySubscribedException {
        cc.setCurrentClub(club6);
        cc.addClubMember(user13);
        assertTrue(club6.isMember(user13));

        AlreadySubscribedException thrown = assertThrows(
                AlreadySubscribedException.class,
                () -> cc.addClubMember(user13)
        );
        assertTrue(thrown.getMessage().contains("Utente già registrato"));
    }
}