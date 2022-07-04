/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import DomainModel.Club;
import DomainModel.UserClub;
import DAO.BookingDao;
import DAO.DaoFactory;
import DomainModel.Person;
import DomainModel.User;
import DomainModel.Sport;

import java.util.Objects;


/**
 *
 * @author Alessio
 */


public class Masi_delmoro_swe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        BookingDao bookingDao = Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getBookingDao();
        Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getClubDao();
        Objects.requireNonNull(DaoFactory.getDaoFactory(1)).getUserDao();
        RequestManager rm = RequestManager.getInstance();


        Club clb1 = new Club("LaFiorita", 9, 23,5);
        Club clb2 = new Club("Gracciano", 30, 10,5);
        Club clb3 = new Club("UPP", 10, 7,5);
        Club clb4 = new Club("Certaldo", 20, 8,5);
        Club clb5 = new Club("Firenze Padel", 18, 9,8);
        
        clb1.addField("Soccer 1", Sport.SOCCER,8);
        clb1.addField("Padel 1", Sport.PADEL,15);
        clb1.addField("Padel 2", Sport.PADEL,15);
        clb2.addField("Soccer 1", Sport.SOCCER,8);
        clb2.addField("Padel 2", Sport.PADEL,15);
        clb3.addField("Soccer 1", Sport.SOCCER,8);
        clb4.addField("Soccer 1", Sport.SOCCER,8);
        clb4.addField("Soccer 2", Sport.SOCCER,8);
        clb4.addField("Padel 3", Sport.PADEL,8);
        clb4.addField("Padel 4", Sport.PADEL,12);
        clb4.addField("Padel 5", Sport.PADEL,12);
        clb4.addField("Padel 6", Sport.PADEL,12);
        clb5.addField("Padel 1", Sport.PADEL,12);
        clb5.addField("Padel 2", Sport.PADEL,12);
        clb5.addField("Padel 3", Sport.PADEL,15);
        
        UserClub userClub1 = clb1.subscribe(rm,100);
        UserClub userClub2 = clb2.subscribe(rm, 200);
        UserClub userClub3 = clb3.subscribe(rm, 120);
        UserClub userClub4 = clb4.subscribe(rm, 150);
        UserClub userClub5 = clb5.subscribe(rm, 90);
        
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
        
        User matti = mattia.subscribe(rm, "matti");
        User france = francesco.subscribe(rm, "france");
        User ludo = ludovico.subscribe(rm, "ludo");
        User martaRos = marta.subscribe(rm, "martaRos");
        User ale = alessia.subscribe(rm, "ale");
        User fede = federica.subscribe(rm, "fede");
        User marcoRos = marco.subscribe(rm, "marcoRos");
        User lore = lorenzo.subscribe(rm, "lore");
        User eli = elisabetta.subscribe(rm, "eli");
        User marti = martina.subscribe(rm, "marti");
        User cami = camilla.subscribe(rm, "cami");
        User matte = matteo.subscribe(rm, "matte");
        
        matti.addFunds(250);
        france.addFunds(500);
        ale.addFunds(20);
        eli.addFunds(12);
        cami.addFunds(500);
        marcoRos.addFunds(100);
        lore.addFunds(600);
        matte.addFunds(100);
        ludo.addFunds(500);
        marti.addFunds(100);
        
        matti.bookField(Sport.SOCCER, "LaFiorita", "26/03/2022", 16, new String[]{"france", "cami", "marti", "ale", "eli", "marcoRos", "lore", "matte", "ludo"});
        matti.blindBook(Sport.PADEL, "LaFiorita", "26/07/2022", 16);

        System.out.println(matti.getBalance());
        cami.viewBookings();
        cami.addMatchResult(new String[]{"france", "cami", "marti", "marcoRos", "eli"}, 1);
        cami.viewRecord();
        ale.viewRecord();

        cami.viewBlindBookings(Sport.PADEL);
        cami.bookSpot(2);
        ludo.bookSpot(2);
        lore.bookSpot(2);
        System.out.println(bookingDao.getBooking(2).getPlayers().size());
        ale.viewBlindBookings(Sport.PADEL);
        lore.deleteBooking(2);
        ale.viewBlindBookings(Sport.PADEL);

        clb1.addMember(federica);
        System.out.println(userClub1.getMember(0).username);
    }
}
