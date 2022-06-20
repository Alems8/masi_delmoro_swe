package DAO;

import User.User;
import User.Person;

import java.util.ArrayList;

public interface UserDao {

    public User getUserById(int id);

    public void addUser(User user);

    public void removeUser(User user);

    public int getUsersSize();

    public boolean containsUser(User user);


    public User getUserByUsername(String s);

    public User getUserByPerson(Person person);
}
