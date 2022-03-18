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
public class Person {
    public String name;
    public String surname;
    public String email;

    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    
    public void subscribe(BookingManager bm, String username) {
        bm.addUser(this, username);
    }
}
