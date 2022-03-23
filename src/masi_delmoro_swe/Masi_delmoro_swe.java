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
        BookingManager bm = new BookingManager();
        Club clb1 = new Club("LaFiorita", 15, 12,9,19);
        Club clb2 = new Club("Gracciano", 30, 25,10,23);
        Club clb3 = new Club("UPP", 10, 7,9,1);
        Club clb4 = new Club("Certaldo", 20, 16,8,20);
        Club clb5 = new Club("Firenze Padel", 18, 15,9,18);
        
        clb1.addField("Padel 1");
        clb1.addField("Padel 2");
        clb1.addField("Padel 3");
        clb2.addField("Padel 1");
        clb2.addField("Padel 2");
        clb3.addField("Padel 1");
        clb4.addField("Padel 1");
        clb4.addField("Padel 2");
        clb4.addField("Padel 3");
        clb4.addField("Padel 4");
        clb4.addField("Padel 5");
        clb4.addField("Padel 6");
        clb5.addField("Padel 1");
        clb5.addField("Padel 2");
        clb5.addField("Padel 3");
        
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
        eli.addFunds(400);
        cami.addFunds(500);
        marcoRos.addFunds(100);
        lore.addFunds(600);
        matte.addFunds(100);
        
        System.out.println(matti.bookField("LaFiorita", "25/03/2022", 15, "france", "ale", "eli"));
        System.out.println(eli.getBalance());
        
        
        System.out.println(eli.bookField("UPP", "28/03/2022", 0, "cami", "marcoRos", "matti"));
        eli.viewBookings();
        System.out.println(eli.getBalance());
        eli.deleteBooking(2);
        eli.viewBookings();
        System.out.println(eli.getBalance());
        
        france.joinClub(clb5);
        france.bookField("Firenze Padel", "22/03/2022", 17, "matte", "ale", "marcoRos");
        System.out.println(france.getBalance());
        
        eli.bookField("Firenze Padel", "22/03/2022", 17, "matte", "ale", "marcoRos"); //Questo funziona
        
        matte.blindBook("Firenze Padel", "23/03/2022", 16);
        
        lore.blindBook("Firenze Padel", "23/03/2022", 16); //PROBLEMA
        
        eli.blindBook("UPP", "23/03/2022", 10);
        
        lore.viewBlindBookings();
        lore.bookSpot(3);
        matte.bookSpot(3);
        matti.bookSpot(3);
        
        matti.viewBookings();
    }
    
}

// LISTA PREFERITI, STORICO PARTITE, PRENOTAZIONE BUIO, AGGIUNTA RISULTATO.