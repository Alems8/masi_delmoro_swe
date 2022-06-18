package DAO;

import User.User;

import java.util.ArrayList;

public interface UserDao {

    public User getUser(int id);

    public void addUser(User user);

    public void removeUser(User user);

    public int getUsersSize();

    public boolean containsUser(User user);



}
