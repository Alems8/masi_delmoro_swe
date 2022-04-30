package masi_delmoro_swe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void addFundsTest() {
        BalanceMonitor monitor = new BalanceMonitor();
        BookingManager bm = new BookingManager(monitor);
        Person marco = new Person("marco", "rossi", "marcorossi@mail.it");
        User mark = marco.subscribe(bm, "marcorossi");
        int expected = 100;
        mark.addFunds(expected);
        assertEquals(expected, mark.getBalance());
    }
}