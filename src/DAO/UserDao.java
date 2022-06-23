package DAO;

import DomainLogic.User;
import DomainLogic.Person;

public interface UserDao {

    User getUserById(int id);

    void addUser(User user);

    void removeUser(User user);

    int getUsersSize();

    boolean containsUser(User user);


    User getUserByUsername(String s);

    User getUserByPerson(Person person);
}
