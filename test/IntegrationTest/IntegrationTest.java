package IntegrationTest;

import BusinessLogic.RequestManager;
import DAO.BookingDao;
import DAO.ClubDao;
import DAO.DaoFactory;
import DAO.UserDao;
import DomainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private static BookingDao bookingDao;
    private static ClubDao clubDao;
    private static UserDao userDao;
    private static RequestManager rm;

    private static Club laFiorita;
    private static Club gracciano;
    private static Club upp;
    private static Club certaldo;
    private static Club firenzePadel;

    private static Person mattia;
    private static Person francesco;
    private static Person ludovico;
    private static Person marta;
    private static Person alessia;
    private static Person federica;
    private static Person marco;
    private static Person lorenzo;
    private static Person elisabetta;
    private static Person martina;
    private static Person camilla;
    private static Person matteo;

    @BeforeAll
    static void createManager() {
        bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        clubDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        userDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        rm = RequestManager.getInstance();

        laFiorita = new Club("LaFiorita", 9, 23,25);
        gracciano = new Club("Gracciano", 10, 18,18);
        upp = new Club("UPP", 9, 23,15);
        certaldo = new Club("Certaldo", 8, 1,20);
        firenzePadel = new Club("Firenze Padel", 18, 9,14);

        mattia = new Person("Mattia","Rossi","matt.ross@gmail.com");
        francesco = new Person("Francesco","Rossi","fra.ross@gmail.com");
        ludovico = new Person("Ludovico","Rossi","lud.ross@gmail.com");
        marta = new Person("Marta","Rossi","mart.ross@gmail.com");
        alessia = new Person("Alessia","Tedeschi","ale.tedesc@gmail.com");
        federica = new Person("Federica","Rossi","fede.ross@gmail.com");
        marco = new Person("Marco","Rossi","marco.ross@gmail.com");
        lorenzo = new Person("Lorenzo","Rossi","lore.ross@gmail.com");
        elisabetta = new Person("Elisabetta","Bianchi","bett.bianchi@gmail.com");
        martina = new Person("Martina","Gialli","marti.giall@gmail.com");
        camilla = new Person("Camilla","Verdi","cam.verdi@gmail.com");
        matteo = new Person("Matteo","Bianchi","matt.bianchi@gmail.com");
    }



    @Test
    void allTest(){
        laFiorita.addField("Soccer 1", Sport.SOCCER,8);
        laFiorita.addField("Padel 1", Sport.PADEL,15);
        laFiorita.addField("Padel 2", Sport.PADEL,15);
        gracciano.addField("Soccer 1", Sport.SOCCER,8);
        gracciano.addField("Padel 1", Sport.PADEL,15);
        upp.addField("Padel 1", Sport.PADEL,8);
        certaldo.addField("Soccer 1", Sport.SOCCER,8);
        certaldo.addField("Soccer 2", Sport.SOCCER,8);
        certaldo.addField("Padel 1", Sport.PADEL,12);
        certaldo.addField("Padel 4", Sport.PADEL,12);
        firenzePadel.addField("Padel 1", Sport.PADEL,12);
        firenzePadel.addField("Padel 2", Sport.PADEL,12);
        firenzePadel.addField("Padel 3", Sport.PADEL,15);

        UserClub userClub1 = laFiorita.subscribe(rm,100);
        UserClub userClub2 = gracciano.subscribe(rm, 200);
        UserClub userClub3 = upp.subscribe(rm, 120);
        UserClub userClub4 = certaldo.subscribe(rm, 150);
        UserClub userClub5 = firenzePadel.subscribe(rm, 90);

        User matti = mattia.subscribe(rm, "matti");
        User france = francesco.subscribe(rm, "france");
        User ludo = ludovico.subscribe(rm, "ludo");
        User marty = marta.subscribe(rm, "marty");
        User ale = alessia.subscribe(rm, "ale");
        User fede = federica.subscribe(rm, "fede");
        User mark = marco.subscribe(rm, "mark");
        User lore = lorenzo.subscribe(rm, "lore");
        User eli = elisabetta.subscribe(rm, "eli");
        User marti = martina.subscribe(rm, "marti");
        User cami = camilla.subscribe(rm, "cami");
        User matte = matteo.subscribe(rm, "matte");


        matti.addFunds(50);
        france.addFunds(150);
        france.joinClub("UPP");
        france.addFavouriteClub("UPP");
        france.addFavouriteClub("Gracciano");
        ludo.addFunds(30);
        marty.addFunds(40);

        //Test di prenotazione effettuata e pagamento effettuato
        matti.bookField(Sport.PADEL, "UPP", "15/08/2022",14,new String[]{"france", "ludo", "marty"});
        assertEquals(1,bookingDao.getBookingsSize());
        assertEquals(23, france.getBalance());
        assertEquals(42, matti.getBalance());
        assertEquals(22, ludo.getBalance());
        assertEquals(32, marty.getBalance());

        ale.addFunds(32);
        fede.addFunds(44);
        mark.addFunds(50);

        //Test di MembersMonitor
        certaldo.addMember(lorenzo);
        assertTrue(userClub4.isMember(lore));

        //Test prenotazione al buio e BalanceMonitor
        lore.addFunds(10);
        lore.viewBlindBookings(Sport.PADEL);
        lore.blindBook(Sport.PADEL, "Certaldo", "12/08/2022", 0);
        assertEquals(2, bookingDao.getBookingsSize());
        assertTrue(bookingDao.getBooking(2).containsUser(lore));
        assertEquals(0, lore.getBalance());

        //Test prenotazione posto
        eli.viewBlindBookings(Sport.PADEL);
        eli.bookSpot(2);
        eli.addFunds(20);
        eli.bookSpot(2);
        assertEquals(8, eli.getBalance());

        //Test di rimborso per prenotazione invalida
        assertEquals(42, matti.getBalance());
        matte.bookField(Sport.SOCCER, "Certaldo", "30/07/2022",21, new String[]{"matti", "marti", "eli", "ale", "ludo", "cami", "france", "mark", "fede"});
        assertEquals(42, matti.getBalance());

        marti.addFunds(20);
        eli.addFunds(20);
        cami.addFunds(40);
        france.addFunds(30);
        matte.addFunds(34);

        //Test cancellazione account
        matte.bookField(Sport.SOCCER, "Certaldo", "30/07/2022",21, new String[]{"matti", "marti", "eli", "ale", "ludo", "cami", "france", "mark", "fede"});
        matte.deleteAccount();
        matte.deleteBooking(3);
        matte.deleteAccount();
        assertFalse(userDao.containsUser(matte));
        eli.bookField(Sport.PADEL, "Certaldo", "18/08/2022", 15, new String[]{"matti", "matte", "mark"});

        //Test prenotazione posto
        mark.viewBlindBookings(Sport.PADEL);
        mark.bookSpot(2);
        fede.viewBlindBookings(Sport.PADEL);
        fede.bookSpot(2);

        matti.viewBlindBookings(Sport.PADEL);

        //Test cancellazione prenotazione
        lore.viewBookings();
        lore.deleteBooking(2);
        assertEquals(10, lore.getBalance());

        //Test prenotazione posto
        matti.viewBlindBookings(Sport.PADEL);
        matti.bookSpot(2);

        //Test aggiunta risultato
        eli.addMatchResult(new String[]{"matti", "fede"}, 2);
        assertEquals(0, matti.record.get(Sport.PADEL)[0]);
        assertEquals(1, matti.record.get(Sport.PADEL)[1]);
        assertEquals(0, fede.record.get(Sport.PADEL)[0]);
        assertEquals(1, fede.record.get(Sport.PADEL)[1]);
        assertEquals(0, eli.record.get(Sport.PADEL)[1]);
        assertEquals(1, eli.record.get(Sport.PADEL)[0]);
        assertEquals(0, mark.record.get(Sport.PADEL)[1]);
        assertEquals(1, mark.record.get(Sport.PADEL)[0]);
        matti.viewBookings();

        //Test cancellazione account
        lore.deleteAccount();
        assertFalse(userDao.containsUser(lore));

        //Test occupazione campo
        eli.bookField(Sport.PADEL, "UPP", "18/08/2022", 15, new String[]{"matti", "fede", "mark"});
        ale.bookField(Sport.PADEL, "UPP", "18/08/2022", 15, new String[]{"cami","marty","ludo"});

    }




}
