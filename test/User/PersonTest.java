package User;

import BusinessLogic.BalanceMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonTest {
    private Person marco, luigi, filippo;
    private BookingManager manager;
    private AbstractBookingManager bm;

    @BeforeEach
    void setUp() {
        BalanceMonitor monitor = new BalanceMonitor();
        this.manager = new BookingManager(monitor);
        this.bm = new BookingChecker(manager);

        this.marco = new Person("marco", "rossi", "marcorossi@mail.it");
        this.luigi = new Person("luigi", "bianchi", "luigibianchi@mail.it");
        this.filippo = new Person("filippo", "pallini", "filippopallini@mail.com");
    }

    @Test
    void subscribe() {
        marco.subscribe(bm, "mark");
        assertEquals(1, manager.getUsersSize());
        luigi.subscribe(bm, "gigi");
        assertEquals(2, manager.getUsersSize());
        luigi.subscribe(bm, "gigio");
        assertEquals(2, manager.getUsersSize());
        filippo.subscribe(bm, "mark");
        assertEquals(2, manager.getUsersSize());
    }
}