package BusinessLogic;

import DAO.DaoFactory;
import DomainModel.Club;
import DomainModel.Person;
import DomainModel.Sport;
import DomainModel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class UserControllerTest {



    @BeforeEach
    void setUp() {
        this.bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        this.clubDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        this.userDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        RequestManager rm = new RequestManager();
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
    void setCurrentPlayers() {

    }

    @Test
    void setCurrentUser() {

    }

    @Test
    void getCurrentUser() {
    }

    @Test
    void checkBalance() {
    }

    @Test
    void payBooking() {
    }

    @Test
    void payJoining() {
    }

    @Test
    void refund() {
    }

    @Test
    void createUser() {
    }

    @Test
    void topUpUserBalance() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void addFavouriteClub() {
    }
}