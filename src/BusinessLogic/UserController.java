package BusinessLogic;

import Club.Field;
import Club.UserClub;
import DAO.*;
import ObserverUtil.Observer;
import User.User;
import User.Person;

import java.util.ArrayList;

public class UserController {
    private final ArrayList<User> currentPlayers = new ArrayList<>();
    private User currentUser;
    private final UserDao userDao;

    public UserController() {
        this.userDao = FakeUserDao.getInstance();
    }


    public void setCurrentPlayers(ArrayList<String> users, int size) throws WrongNameException, NoFreeSpotException {
        currentPlayers.clear();
        if (users.size() != size)
            throw new NoFreeSpotException();
        for(int i=0; i<size; i++){
            User u = userDao.getUserByUsername(users.get(i));
            if (u == null)
                throw new WrongNameException();

            currentPlayers.add(u);
        }
    }

    ArrayList<User> getCurrentPlayers(){
        return currentPlayers;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }
    public User getCurrentUser(){
        return currentUser;
    }

    boolean checkBalance(int price){
        return currentUser.getBalance() >= price;
    }

    public void payBooking(UserClub club, Field field) throws LowBalanceException {
        int price = field.price;
        if(club.isMember(currentUser))
            price = price - price*(club.getClub().memberDiscount)/100;

        if(!checkBalance(price))
            throw new LowBalanceException();

        currentUser.setBalance(currentUser.getBalance() - price);
    }

    void payJoining(UserClub club) throws LowBalanceException {
        int price = club.joinClubPrice;
        if(!checkBalance(price))
            throw new LowBalanceException();
        currentUser.setBalance(currentUser.getBalance() - price);
    }

    public void refund(int price) {
        currentUser.setBalance(currentUser.getBalance() + price);
    }

    private void checkPerson(Person person) throws AlreadySubscribedException{
        if(userDao.getUserByPerson(person) != null)
            throw new AlreadySubscribedException();
    }

    private void checkUser(String username) throws WrongNameException {
        if (userDao.getUserByUsername(username) != null)
            throw new WrongNameException();
    }

    User createUser(Person person, String username, RequestManager rm){
        try{checkPerson(person);}
        catch(AlreadySubscribedException e){
            System.out.println("Sei già registrato al servizio");
            return null;
        }
        try {checkUser(username);}
        catch (WrongNameException e){
            System.out.println("L'username scelto è già utilizzato");
            return null;
        }
        User user = new User(username, person, rm);
        Observer monitor = BalanceMonitor.getInstance();
        user.addObserver(monitor);
        userDao.addUser(user);
        return user;
    }

    public void topUpUserBalance(User user, int money){
        setCurrentUser(user);
        currentUser.setBalance(currentUser.getBalance() + money);
    }

    public void deleteUser() {
        userDao.removeUser(currentUser);
    }

    public void addFavouriteClub() {
        //currentUser.getFavouriteClubs().add()
    }

}
