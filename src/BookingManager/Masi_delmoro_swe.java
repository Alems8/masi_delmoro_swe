/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookingManager;

import Club.Club;
import Person.Person;
import Sport.Sport;
import Sport.Padel;
import Sport.Soccer;

import java.awt.print.Book;

/**
 *
 * @author Alessio
 */

//SENZA SCANNER
public class Masi_delmoro_swe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LowBalanceException {
        // TODO code application logic here
        Sport soccer = new Soccer();
        Sport padel = new Padel();
        BalanceMonitor monitor = new BalanceMonitor();
        BookingManager manager = new BookingManager(monitor);
        AbstractBookingManager bm = new BookingChecker(manager);
        Club clb1 = new Club("LaFiorita", 9, 23,5);
        Club clb2 = new Club("Gracciano", 30, 10,5);
        Club clb3 = new Club("UPP", 10, 7,5);
        Club clb4 = new Club("Certaldo", 20, 8,5);
        Club clb5 = new Club("Firenze Padel", 18, 9,8);
        
        clb1.addField("Soccer 1", soccer,8);
        clb1.addField("Padel 2", padel,15);
        clb1.addField("Padel 3", padel,15);
        clb2.addField("Soccer 1", soccer,8);
        clb2.addField("Padel 2", padel,15);
        clb3.addField("Soccer 1", soccer,8);
        clb4.addField("Soccer 1", soccer,8);
        clb4.addField("Soccer 2", soccer,8);
        clb4.addField("Padel 3", padel,8);
        clb4.addField("Padel 4", padel,12);
        clb4.addField("Padel 5", padel,12);
        clb4.addField("Padel 6", padel,12);
        clb5.addField("Padel 1", padel,12);
        clb5.addField("Padel 2", padel,12);
        clb5.addField("Padel 3", padel,15);
        
        clb1.subscribe(bm,100);
        clb2.subscribe(bm, 200);
        clb3.subscribe(bm, 120);
        clb4.subscribe(bm, 150);
        clb5.subscribe(bm, 90);
        
        Person mattia = new Person("Mattia","Rossi","matt.rossi@gmail.com");
        Person francesco = new Person("Francesco","Rossi","fra.rossi@gmail.com");
        Person ludovico = new Person("Ludovico","Rossi","lud.rossi@gmail.com");
        Person marta = new Person("Marta","Rossi","mart.rossi@gmail.com");
        Person alessia = new Person("Alessia","Tedeschi","ale.tedeschi@gmail.com");
        Person federica = new Person("Federica","Rossi","fede.rossi@gmail.com");
        Person marco = new Person("Marco","Rossi","marco.rossi@gmail.com");
        Person lorenzo = new Person("Lorenzo","Rossi","lore.rossi@gmail.com");
        Person elisabetta = new Person("Elisabetta","Bianchi","betti.bianchi@gmail.com");
        Person martina = new Person("Martina","Gialli","marti.gialli@gmail.com");
        Person camilla = new Person("Camilla","Verdi","cami.verdi@gmail.com");
        Person matteo = new Person("Matteo","Bianchi","matte.bianchi@gmail.com");
        
        User matti = mattia.subscribe(bm, "matti");
        User france = francesco.subscribe(bm, "france");
        User ludo = ludovico.subscribe(bm, "ludo");
        User martaRos = marta.subscribe(bm, "martaRos");
        User ale = alessia.subscribe(bm, "ale");
        User fede = federica.subscribe(bm, "fede");
        User marcoRos = marco.subscribe(bm, "marcoRos");
        User lore = lorenzo.subscribe(bm, "lore");
        User eli = elisabetta.subscribe(bm, "eli");
        User marti = martina.subscribe(bm, "marti");
        User cami = camilla.subscribe(bm, "cami");
        User matte = matteo.subscribe(bm, "matte");
        
        matti.addFunds(250);
        france.addFunds(500);
        ale.addFunds(20);
        eli.addFunds(24);
        cami.addFunds(500);
        marcoRos.addFunds(100);
        lore.addFunds(600);
        matte.addFunds(100);
        
        matti.bookField(padel, "LaFiorita", "26/03/2022", 16,"cami","eli","ale");
        cami.viewBookings();
        cami.addMatchResult("eli", "ale", 1);
        cami.viewRecord();
        ale.viewRecord();
    }
}
