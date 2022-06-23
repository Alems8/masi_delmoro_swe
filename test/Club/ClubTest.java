package Club;

import BusinessLogic.BalanceMonitor;
import DomainLogic.Club;
import DomainLogic.Person;
import DomainLogic.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClubTest {
    private Club clb1, clb2, clb3, clb4, clb5;
    private final Sport padel = new Padel();
    private final Sport soccer = new Soccer();
    private AbstractBookingManager bm;
    private BookingManager manager;
    private Person mattia, marco;


    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.manager = new BookingManager(monitor);
        this.bm = new BookingChecker(manager);
        this.clb1 = new Club("LaFiorita", 5, 19,3);
        this.clb2 = new Club("Gracciano", 3, 10,3);
        this.clb3 = new Club("UPP", 10, 17,2);
        this.clb4 = new Club("Certaldo", 2, 18,2);
        this.clb5 = new Club("Firenze Padel", 8, 19,8);

        this.mattia = new Person("mattia", "verdi", "mattiaverdi@mail.it");
        this.marco = new Person("marco", "rossi", "marcorossi@mail.it");

    }
    @Test
    void subscribe() {
        clb1.subscribe(bm, 100);
        clb2.subscribe(bm, 200);
        clb3.subscribe(bm,90);
        clb4.subscribe(bm,100);
        clb5.subscribe(bm,150);
        assertEquals(5, manager.getClubsSize());
    }

    @Test
    void addField() {
        clb1.addField("Padel 1", padel,12);
        clb1.addField("Soccer 1", soccer,5);
        clb2.addField("Padel 1", padel,15);
        clb2.addField("Padel 2", padel,12);
        assertEquals(2, clb1.fields.size());
        assertEquals(2, clb1.fields.size());
    }

    @Test
    void addMember() {
        clb1.addMember(mattia);
        assertTrue(clb1.isMember(mattia));
    }

    @Test
    void getMember() {
        clb1.addMember(mattia);
        clb1.addMember(marco);
        assertEquals(mattia, clb1.getMember(0));
        assertEquals(marco, clb1.getMember(1));
    }

    @Test
    void getMembersSize() {
        assertEquals(0, clb1.getMembersSize());
        clb1.addMember(mattia);
        clb1.addMember(marco);
        assertEquals(2, clb1.getMembersSize());
    }

    @Test
    void isMember() {
        clb1.addMember(mattia);
        assertTrue(clb1.isMember(mattia));
    }


}