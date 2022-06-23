package DAO;

import DomainLogic.User;
import DomainLogic.Person;

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
    public User getUserById(int id){
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

    @Override
    public User getUserByUsername(String usernm) {
        for (User u : users) {
            if (u.username.equals(usernm)) {
                return u;
            }
        }
        return null;

    }

    @Override
    public User getUserByPerson(Person person){
        for (User u : users){
            if (u.getPerson().equals(person))
                return u;
        }
        return null;
    }

}
