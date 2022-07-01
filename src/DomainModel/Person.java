/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainModel;

import BusinessLogic.AlreadySubscribedException;
import BusinessLogic.RequestManager;
import BusinessLogic.WrongNameException;

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
    
    public User subscribe(RequestManager rm, String username) {
        User user = rm.addUser(this, username);
        return user;
    }
}
