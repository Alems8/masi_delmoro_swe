package BusinessLogic;


import DAO.DaoFactory;
import DAO.UserDao;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Objects;

class UserControllerTest {
    private static RequestManager rm;
    private static UserDao userDao;
    private static User user1, user2, user3;
    private static UserClub club1;
    private static UserController uc;


    @BeforeAll
    static void setUp() {

        userDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        rm = RequestManager.getInstance();
        uc = rm.uc;
        Person lorenzo = new Person("Lorenzo","Rossi","lore.rossi@gmail.com");
        Person elisabetta = new Person("Elisabetta","Bianchi","betti.bianchi@gmail.com");
        Person martina = new Person("Martina","Gialli","marti.gialli@gmail.com");
        user1 = lorenzo.subscribe(rm, "lorens");
        user2 = elisabetta.subscribe(rm, "elys");
        user3 = martina.subscribe(rm, "martins");
        Club clb1 = new Club("Signa", 9, 23,5);
        clb1.addField("Padel 1", Sport.PADEL, 5);
        club1 = clb1.subscribe(rm, 100);
    }

    @Test
    void setCurrentPlayers() throws WrongNameException, NoFreeSpotException {
        ArrayList<String> users = new ArrayList<>(){{add("lorens");add("martins");}};
        NoFreeSpotException thrown = assertThrows(
                NoFreeSpotException.class,
                () -> uc.setCurrentPlayers(users,3)
        );
        assertTrue(thrown.getMessage().contains("Nessun posto disponibile"));

        uc.setCurrentPlayers(users,2);
        assertEquals(2,uc.getCurrentPlayers().size());

        users.add("fabri");
        WrongNameException thrown2 = assertThrows(
                WrongNameException.class,
                () -> uc.setCurrentPlayers(users,3)
        );
        assertTrue(thrown2.getMessage().contains("Il nome inserito non è coretto"));

    }

    @Test
    void setCurrentUser() {
        uc.setCurrentUser(user1);
        assertEquals(user1,uc.getCurrentUser());
    }

    @Test
    void checkBalance() {
        user1.setBalance(100);
        uc.setCurrentUser(user1);
        assertTrue(uc.checkBalance(50));
        assertTrue(uc.checkBalance(100));
        assertFalse(uc.checkBalance(120));
    }

    @Test
    void payBooking() throws LowBalanceException {
        uc.setCurrentUser(user1);
        user1.setBalance(110);
        uc.payBooking(club1, club1.getClub().fields.get(0));
        assertEquals(105, uc.getCurrentUser().getBalance());

        user1.joinClub("Signa");
        uc.payBooking(club1, club1.getClub().fields.get(0));
        assertEquals(0, uc.getCurrentUser().getBalance());

        uc.setCurrentUser(user2);
        LowBalanceException thrown = assertThrows(
                LowBalanceException.class,
                () -> uc.payBooking(club1, club1.getClub().fields.get(0))
        );
        assertTrue(thrown.getMessage().contains("Non hai abbastanza fondi per una prenotazione"));
    }

    @Test
    void payJoining() throws LowBalanceException {
        uc.setCurrentUser(user1);
        user1.setBalance(110);
        uc.payJoining(club1);
        assertEquals(10, uc.getCurrentUser().getBalance());

        uc.setCurrentUser(user2);
        LowBalanceException thrown = assertThrows(
                LowBalanceException.class,
                () -> uc.payBooking(club1, club1.getClub().fields.get(0))
        );
        assertTrue(thrown.getMessage().contains("Non hai abbastanza fondi per una prenotazione"));
    }

    @Test
    void refund() {
        uc.setCurrentUser(user1);
        user1.setBalance(110);
        uc.refund(20);
        assertEquals(130,uc.getCurrentUser().getBalance());
    }

    @Test
    void createUser() {
        int exp = userDao.getUsersSize() + 1;
        Person marte = new Person("Marte","Rossi","marte.rossi@gmail.com");
        User user5 = uc.createUser(marte,"martyz",rm);
        assertEquals(exp,userDao.getUsersSize());
        assertTrue(userDao.containsUser(user5));
    }

    @Test
    void topUpUserBalance() {
        uc.topUpUserBalance(user3,20);
        assertEquals(20, uc.getCurrentUser().getBalance());
    }

    @Test
    void deleteUser() {
        uc.setCurrentUser(user2);
        uc.deleteUser();
        assertFalse(userDao.containsUser(user2));
    }

    @Test
    void addFavouriteClub() {
        uc.setCurrentUser(user1);
        uc.addFavouriteClub(club1);

        assertTrue(uc.getCurrentUser().getFavouriteClubs().contains(club1));
    }
}