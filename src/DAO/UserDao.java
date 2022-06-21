package DAO;

import User.User;
import User.Person;

public interface UserDao {

    User getUserById(int id);

    void addUser(User user);

    void removeUser(User user);

    int getUsersSize();

    boolean containsUser(User user);


    User getUserByUsername(String s);

    User getUserByPerson(Person person);
}
