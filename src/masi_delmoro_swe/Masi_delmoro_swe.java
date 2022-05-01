/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

/**
 *
 * @author Alessio
 */
public class Masi_delmoro_swe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Sport soccer = new Soccer();
        Sport padel = new Padel();
        BalanceMonitor monitor = new BalanceMonitor();
        
        BookingManager bm = new BookingManager(monitor);
        Club clb1 = new Club("LaFiorita", 15, 12,9,19,100);
        Club clb2 = new Club("Gracciano", 30, 25,10,23,100);
        Club clb3 = new Club("UPP", 10, 7,9,1,120);
        Club clb4 = new Club("Certaldo", 20, 16,8,20,150);
        Club clb5 = new Club("Firenze Padel", 18, 15,9,18,90);
        
        clb1.addField("Soccer 1", soccer);
        clb1.addField("Padel 2", padel);
        clb1.addField("Padel 3", padel);
        clb2.addField("Soccer 1", soccer);
        clb2.addField("Padel 2", padel);
        clb3.addField("Soccer 1", soccer);
        clb4.addField("Soccer 1", soccer);
        clb4.addField("Soccer 2", soccer);
        clb4.addField("Padel 3", padel);
        clb4.addField("Padel 4", padel);
        clb4.addField("Padel 5", padel);
        clb4.addField("Padel 6", padel);
        clb5.addField("Padel 1", padel);
        clb5.addField("Padel 2", padel);
        clb5.addField("Padel 3", padel);
        
        clb1.subscribe(bm);
        clb2.subscribe(bm);
        clb3.subscribe(bm);
        clb4.subscribe(bm);
        clb5.subscribe(bm);
        
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
        ale.addFunds(100);
        eli.addFunds(25);
        cami.addFunds(500);
        marcoRos.addFunds(100);
        lore.addFunds(600);
        matte.addFunds(100);
        
        matti.bookField(padel, "Firenze Padel", "26/03/2022", 16);
        System.out.println(cami.deleteBooking());
        cami.viewBookings();
    }
}
