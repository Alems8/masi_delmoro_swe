/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import masi_delmoro_swe.BalanceMonitor;
import masi_delmoro_swe.BalanceMonitor;
import masi_delmoro_swe.BookingManager;
import masi_delmoro_swe.BookingManager;
import masi_delmoro_swe.Person;
import masi_delmoro_swe.Person;
import masi_delmoro_swe.Sport;
import masi_delmoro_swe.Sport;
import masi_delmoro_swe.User;
import masi_delmoro_swe.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author thomas
 */
public class UserTest {
    
    public UserTest() {     
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getBalance method, of class User.
     */
    @Test
    public void testGetBalance() {
        BalanceMonitor monitor = new BalanceMonitor();
        BookingManager bm = new BookingManager(monitor);
        Person mattia = new Person("mattia", "rossi", "kshfso");   
        User matti = mattia.subscribe(bm, "mattiarossi");
        int expected = 100;
        matti.addFunds(expected);
        assertEquals(expected, matti.getBalance());
    }

    /**
     * Test of setBalance method, of class User.
     */
    @Test
    public void testSetBalance() {
        System.out.println("setBalance");
        int balance = 0;
        User instance = null;
        instance.setBalance(balance);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of joinClub method, of class User.
     */
    @Test
    public void testJoinClub() {
        System.out.println("joinClub");
        String club = "";
        User instance = null;
        boolean expResult = false;
        boolean result = instance.joinClub(club);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of bookField method, of class User.
     */
    @Test
    public void testBookField() {
        System.out.println("bookField");
        Sport sport = null;
        String clb = "";
        String date = "";
        int hour = 0;
        User instance = null;
        boolean expResult = false;
        boolean result = instance.bookField(sport, clb, date, hour);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addFunds method, of class User.
     */
    @Test
    public void testAddFunds() {
        System.out.println("addFunds");
        int money = 0;
        User instance = null;
        instance.addFunds(money);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteBooking method, of class User.
     */
    @Test
    public void testDeleteBooking() {
        System.out.println("deleteBooking");
        User instance = null;
        instance.deleteBooking();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewBookings method, of class User.
     */
    @Test
    public void testViewBookings() {
        System.out.println("viewBookings");
        User instance = null;
        instance.viewBookings();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of blindBook method, of class User.
     */
    @Test
    public void testBlindBook() {
        System.out.println("blindBook");
        Sport sport = null;
        String clb = "";
        String date = "";
        int hour = 0;
        User instance = null;
        boolean expResult = false;
        boolean result = instance.blindBook(sport, clb, date, hour);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewBlindBookings method, of class User.
     */
    @Test
    public void testViewBlindBookings() {
        System.out.println("viewBlindBookings");
        User instance = null;
        instance.viewBlindBookings();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of bookSpot method, of class User.
     */
    @Test
    public void testBookSpot() {
        System.out.println("bookSpot");
        int key = 0;
        User instance = null;
        boolean expResult = false;
        boolean result = instance.bookSpot(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addFavouriteClub method, of class User.
     */
    @Test
    public void testAddFavouriteClub() {
        System.out.println("addFavouriteClub");
        String club = "";
        User instance = null;
        boolean expResult = false;
        boolean result = instance.addFavouriteClub(club);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addMatchResult method, of class User.
     */
    @Test
    public void testAddMatchResult() {
        System.out.println("addMatchResult");
        String winner1 = "";
        String winner2 = "";
        int key = 0;
        User instance = null;
        instance.addMatchResult(winner1, winner2, key);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewRecord method, of class User.
     */
    @Test
    public void testViewRecord() {
        System.out.println("viewRecord");
        User instance = null;
        instance.viewRecord();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of subscribe method, of class User.
     */
    @Test
    public void testSubscribe() {
        System.out.println("subscribe");
        User instance = null;
        instance.subscribe();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unsubscribe method, of class User.
     */
    @Test
    public void testUnsubscribe() {
        System.out.println("unsubscribe");
        User instance = null;
        instance.unsubscribe();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of notifyChanges method, of class User.
     */
    @Test
    public void testNotifyChanges() {
        System.out.println("notifyChanges");
        User instance = null;
        instance.notifyChanges();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAccount method, of class User.
     */
    @Test
    public void testDeleteAccount() {
        System.out.println("deleteAccount");
        User instance = null;
        boolean expResult = false;
        boolean result = instance.deleteAccount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
