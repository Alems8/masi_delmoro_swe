/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Person;

import BookingManager.BookingManager;
import BookingManager.User;
import BookingManager.WrongNameException;

/**
 *
 * @author Alessio
 */
public class Person {
    public String name;
    public String surname;
    public String email;

    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    
    public User subscribe(BookingManager bm, String username) {
        User user = null;
        try{user = bm.addUser(this, username);}
        catch(WrongNameException e) {
            System.out.println("Il nome utente non è disponibile");
        }
        return user;
    }
}
