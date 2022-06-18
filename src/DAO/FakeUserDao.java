package DAO;

import User.User;

import java.util.ArrayList;

public class FakeUserDao implements UserDao{
    private final ArrayList<User> users;
    private static FakeUserDao instance = null;

    private FakeUserDao() {
        users = new ArrayList<>();
    }

    public static FakeUserDao getInstance(){
        if (instance == null){
            instance = new FakeUserDao();
        }
        return instance;
    }

    @Override
    public User getUser(int id){
        return users.get(id);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void removeUser(User user){
        users.remove(user);
    }

    @Override
    public int getUsersSize(){
        return users.size();
    }

    @Override
    public boolean containsUser(User user){
        return users.contains(user);
    }
}
