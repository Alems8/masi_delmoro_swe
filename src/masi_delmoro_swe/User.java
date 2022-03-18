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
public class User {
    public String username;
    private String name;
    private String surname;
    private String email;
    private BookingManager bm;
    private int balance = 0;

    public User(String username, Person person, BookingManager bm) {
        this.username = username;
        this.name = person.name;
        this.surname = person.surname;
        this.email = person.email;
        this.bm = bm;
    }
    
    
    public boolean sendRequest(Club club) {
        //Pagamento costo associazione
        return club.getRequest(this);
    }
    
    public boolean bookField(String clb, String date, int hour, String user2, String user3, String user4) {
       return bm.requestBooking(clb, date, hour,this.username, user2, user3, user4);
    }
}
